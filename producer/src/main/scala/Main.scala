import org.apache.spark.sql.SparkSession

object Main extends App {
    def main(args: Array[String]): Unit = {
      val spark = SparkSession

      Producer.run()
    }

}
