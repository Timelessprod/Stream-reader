import org.apache.spark.sql.SparkSession

object Main extends App {
  val spark = SparkSession.builder().appName("analyzer").config("spark.master", "local[*]").getOrCreate()
  spark.read.csv("hdfs://localhost:9000/drone-reports").show()
}