import java.util.Properties
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.Serdes
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig

class Stream_Kafka(input: DroneReport) {
    // creation builder
    val builder: StreamsBuilder = new StreamsBuilder

    // Serde = Serializer/Deserializer : DroneReport <=> JSON
    val droneRepSerde = new Serdes[DroneReport]

    // Producer
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes[String])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, droneRepSerde)
    val producer = new KafkaProducer[String, DroneReport](props)

    def sendReport(droneReport: DroneReport) = {
        val record = new ProducerRecord[String, DroneReport]("drone-report", droneReport.reportID, droneReport)
        producer.send(record)

        // producer.close()
    }

    // balance les droneReports dans la stream
    val droneRepStream = builder.stream[Id[DroneReport], DroneReport]("drone-report")

    // etapes pour envoyer un report (a commenter car la ca le fait 1 fois pour l'exemple)
    val droneRep = DroneReport(123456789, 987654321, "2016-01-01T00:00:00.000Z", 0.0, 0.0, Array("Hello", "I", "am", "a", "PeaceWatcher"), Map(Array(147852369, 10)))
}