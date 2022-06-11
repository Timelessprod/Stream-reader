import org.apache.spark.sql.functions.{explode, from_json, schema_of_json}
import org.apache.spark.sql.types.{ArrayType, DoubleType, IntegerType, MapType, StringType, StructField, StructType}
import org.apache.spark.sql.{Encoders, SparkSession, types}

object ConsumerSpark {
  val topic = "drone-report"

  val topics: Array[String] = Array(this.topic)

  val bootstrapServer: String = "localhost:9092"
  val spark: SparkSession = SparkSession.builder().appName("KafkaConsumerSpark").master("local[*]").getOrCreate()

  import spark.implicits._

  val schema: StructType = Encoders.product[DroneReport].schema

  val schema2: StructType = StructType(Array(
    StructField("reportId", IntegerType, nullable = false),
    StructField("peaceWatcherId", IntegerType, nullable = false),
    StructField("time", StringType, nullable = false),
    StructField("latitude", DoubleType, nullable = false),
    StructField("longitude", DoubleType, nullable = false),
    StructField("heardWords", ArrayType(StringType, containsNull = false), nullable = false),
    StructField("peaceScores", MapType(StringType, StringType, valueContainsNull = true), nullable = true)
  ))

  def consumeAndWrite(): Unit = {
    println("Start consuming and writing")
    spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServer)
      .option("subscribe", topic)
      .option("startingOffsets", "earliest")
      //.option("endingOffsets", "latest")
      .load()
      //.show()
      //.write.format("csv")
      //.write.format("com.databricks.spark.csv")
      //.save("hdfs://localhost:9000/drone-reports_test")
      //.option("checkpointLocation", "hdfs://localhost:9000/checkpoint")
      //.option("path", "hdfs://localhost:9000/drone-reports")
      .select(from_json($"value".cast("string"), schema2).as("data"))
      .select("data.*")
      //.show()
      .select($"reportId", $"peaceWatcherId", $"time", $"longitude", $"latitude",
              explode($"heardWords").as("heardWords"), $"peaceScores")
      .select($"reportId", $"peaceWatcherId", $"time", $"longitude", $"latitude",
        $"heardWords", explode($"peaceScores"))
      .withColumnRenamed("key", "citizenId")
      .withColumnRenamed("value", "peaceScore")
      .writeStream.format("csv")
      .option("checkpointLocation", "hdfs://localhost:9000/checkpoint")
      .option("path", "hdfs://localhost:9000/drone-reports")
      .start()
      .awaitTermination()
    println("Done consuming and writing")
  }
}
