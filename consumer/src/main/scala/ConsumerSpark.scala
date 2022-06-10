import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.dstream.DStream

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.functions.from_json

object ConsumerSpark {
    val sparkConf: SparkConf = new SparkConf().setAppName("KafkaConsumerSpark").setMaster("local[*]")

    val streamingContext = new StreamingContext(sparkConf, Seconds(1))

    val topic = "drone-report"

    val topics = new TopicPartition(this.topic, 1)

    val kafkaParams: Map[String, Object] = Map[String, Object](
        "bootstrap.servers" -> "localhost:9092",
        "key.deserializer" -> classOf[StringDeserializer],
        "value.deserializer" -> classOf[StringDeserializer],
        "group.id" -> "use_a_separate_group_id_for_each_stream",
        "auto.offset.reset" -> "latest",
        "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val stream: DStream[String] = KafkaUtils.createDirectStream[String, String](
        streamingContext,
        PreferConsistent,
        Subscribe[String, String](topics, kafkaParams)
    ).map(record => record.value)
}
