import org.apache.kafka.streams.scala.Serdes._
import com.goyeau.kafka.streams.circe.CirceSerdes._

class Stream_Kafka(input: DroneReport) {
    // creation builder
    val builder: StreamsBuilder = new StreamsBuilder

    // Serde = Serializer/Deserializer : DroneReport <=> JSON
    val droneRepSerde = new JsonSerde[DroneReport]

    // balance les droneReports dans la stream
    val droneRepStream = builder.stream[Id[DroneReport], DroneReport]("drone-report")

    // creation producer
    val config = new Properties()
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    val producer = Producer[DroneReport](config)

    // etapes pour envoyer un report (a commenter car la ca le fait 1 fois pour l'exemple)
    val droneRep = DroneReport("123456789012", "210987654321", "2016-01-01T00:00:00.000Z", 0.0, 0.0, Array("Hello", "I", "am", "a", "PeaceWatcher"), )
}