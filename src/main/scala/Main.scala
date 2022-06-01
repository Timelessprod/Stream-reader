
object Main extends App {
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
        read.option("header", true).csv(filename)

    // same thing but for JSON file
    def read_json(filename: String): DataFrame =
        read.option("header", true).json(filename)

    // read a file (csv/JSON) and create an array of drones
    def read(filename: String) = {
        if (filename.endsWith(".csv"))
            df = read_csv(filename)
        else if (filename.endsWith(".json"))
            df = read_json(filename)

        
    }


}
