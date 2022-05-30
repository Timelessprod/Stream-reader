import SparkSession.read

class Drone {

  // read a csv from the pathfile and return a DataFrame
  // +----+----+
  // |ColA|ColB|
  // +----+----+
  // |   1|   2|
  // |   3|   4|
  // |   5|   6|
  // |   7|   8|
  // +----+----+
  def read_csv(filename: String): DataFrame = 
    read.csv(filename)

  // same thing but for JSON file
  def read_json(filename: String): DataFrame =
    read.json(filename)
}