import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, MapType, StringType, StructField, StructType}

object Main {
    def main(args: Array[String]): Unit = {
        Consumer.consumeAndWrite()
    }
}
