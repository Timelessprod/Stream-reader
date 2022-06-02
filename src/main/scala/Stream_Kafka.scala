import java.util.Properties
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.Serdes
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import scala.collection.JavaConverters._
import java.time.Duration
import org.apache.kafka.clients.consumer.ConsumerRecords

class Stream_Kafka(input: DroneReport) {
    // creation builder
    val builder: StreamsBuilder = new StreamsBuilder

    // Serde = Serializer/Deserializer : DroneReport <=> JSON
    val droneRepSerde = new Serdes[DroneReport]

    // Producer
    def sendReport(droneReport: DroneReport, producer: KafkaProducer) = {
        val record = new ProducerRecord[String, DroneReport]("drone-report", droneReport.reportID, droneReport)
        producer.send(record)
    }

    def sendRecords(droneReports: List[DroneReport]) = {
        val props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes[String])
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, droneRepSerde)
        val producer = new KafkaProducer[String, DroneReport](props)

        droneReports.foreach { record => sendReport(record, producer) }

        producer.close()
    }

    // Consumer
    def receiveReport() = {
        val props = new Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serdes[String])
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, droneRepSerde)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "myconsumergroup")
        val consumer = new KafkaConsumer[String, DroneReport](props)
        consumer.subscribe(List("drone-report").asJava)

        val records: ConsumerRecords[String, DroneReport] = consumer.poll(Duration.ofMillis(100))   // ca c'est pour la pate chaude (la pate froide c'est plutot 15min)
        records.asScala.foreach { record =>
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
        }
    }

    // balance les droneReports dans la stream
    val droneRepStream = builder.stream[Id[DroneReport], DroneReport]("drone-report")

    // etapes pour envoyer un report (a commenter car la ca le fait 1 fois pour l'exemple)
    // val droneRep = DroneReport(123456789, 987654321, "2016-01-01T00:00:00.000Z", 0.0, 0.0, Array("Hello", "I", "am", "a", "PeaceWatcher"), Map(Array(147852369, 10)))
    
}