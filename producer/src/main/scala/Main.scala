import os.read
import ujson._

object Main extends App {
    def read_json(filename: String) = {
        val jsonString = os.read(filename)
        val data = ujson.read(jsonString)
        data.value
    }

    def main(args: Array[String]) = {
        val test = read_json("test.json")
        println(test)
    }
}
