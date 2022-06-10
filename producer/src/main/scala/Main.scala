object Main {
    def main(args: Array[String]): Unit = {
        if (args.length > 0)
            Producer.run(args(0))
        else
            Producer.run("../json/s1.json")
    }
}