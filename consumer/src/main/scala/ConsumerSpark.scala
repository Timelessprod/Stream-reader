import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.DStream
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.types.StructType

object ConsumerSpark {
    // val sparkConf: SparkConf = new SparkConf().setAppName("KafkaConsumerSpark").setMaster("local[*]")

    // val streamingContext = new StreamingContext(sparkConf, Seconds(1))

    val topic = "drone-report"

    val topics: Array[String] = Array(this.topic)

    // val kafkaParams: Map[String, Object] = Map[String, Object](
    //     "bootstrap.servers" -> "localhost:9092",
    //     "key.deserializer" -> classOf[StringDeserializer],
    //     "value.deserializer" -> classOf[StringDeserializer],
    //     "group.id" -> "use_a_separate_group_id_for_each_stream",
    //     "auto.offset.reset" -> "latest",
    //     "enable.auto.commit" -> (false: java.lang.Boolean)
    // )
    //
    // val stream: DStream[String] = KafkaUtils.createDirectStream[String, String](
    //     streamingContext,
    //     PreferConsistent,
    //     Subscribe[String, String](topics, kafkaParams)
    // ).map(record => record.value)

    // 2nd method

    val bootstrapServer: String = "localhost:9092"
    val spark: SparkSession = SparkSession.builder().appName("KafkaConsumerSpark").master("local[*]").getOrCreate()

    import spark.implicits._

    val schema: StructType = Encoders.product[DroneReport].schema

    def consumeAndWrite(): Unit = {
        println("Start consuming and writing")
        spark.readStream
             .format("kafka")
             .option("kafka.bootstrap.servers", bootstrapServer)
             .option("subscribe", topic)
             .option("startingOffsets", "earliest")
             .load()
             .select(from_json($"value".cast("string"), schema).as("data"))
             .select("data.*")
             .writeStream
             .format("parquet")
             .option("checkpointLocation", "hdfs://localhost:8080/checkpoint")
             .option("path", "hdfs://localhost:8080/drone-reports")
             .start()
             .awaitTermination()
        println("Done consuming and writing")
    }
}
