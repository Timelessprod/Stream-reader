import vegas._
import vegas.data.External._
import vegas.render.WindowRenderer._
import org.apache.spark.sql.SparkSession

object Main extends App {
  val spark = SparkSession.builder().appName("analyzer").config("spark.master", "local[*]").getOrCreate()
  import spark.implicits._

  def readData() = {
    val df = this.spark.read.options(Map("inferSchema"->"true","delimiter"->",","header"->"true"))
  .csv("../json/s1.csv")
    df.show
    df
  }
  val df = readData()
  val plot = Vegas("Lieux reports").
    withData(
      Seq(
        
        Map("country" -> "USA", "population" -> 314),
        Map("country" -> "UK", "population" -> 64),
        Map("country" -> "DK", "population" -> 80)
      )
    ).
    encodeX("country", Nom).
    encodeY("population", Quant).
    mark(Bar)

  // plot.window.show
  // println(plot.html.pageHTML())
}