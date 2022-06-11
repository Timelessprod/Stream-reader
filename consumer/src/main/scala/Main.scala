import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, MapType, StringType, StructField, StructType}

object Main {
    def main(args: Array[String]): Unit = {
        ConsumerSpark.consumeAndWrite()
        // Consumer.run()

        // val spark: SparkSession = SparkSession.builder().appName("KafkaConsumerSpark").master("local[*]").getOrCreate()
        //
        // val schema: StructType = StructType(Array(
        //     StructField("id", IntegerType, nullable = true),
        //     StructField("name", StringType, nullable = true),
        //     StructField("map", MapType(StringType, StringType, valueContainsNull = false))
        // ))
        //
        // val data = Seq(
        //     Row(1, "jean", Map("47457" -> "32453", "12345" -> "67890")),
        //     Row(2, "pierre", Map("34574" -> "23545", "12345" -> "67890")),
        //     Row(3, "pernault", Map("45374" -> "32345", "12345" -> "67890"))
        // )
        //
        // val df = spark.createDataFrame(
        //     spark.sparkContext.parallelize(data),
        //     schema
        // )
        //
        // df.printSchema()
        // df.show()
        // df.coalesce(1).write.json("/home/adrien/test.json")
        //
        // spark.read.schema(schema).json("/home/adrien/test.json").show()
    }
}
