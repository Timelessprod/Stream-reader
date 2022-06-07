import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.json4s.native.Serialization


object Producer extends App {
    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
    // props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serialization)
    // props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serialization)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    def sendReport(droneReport: DroneReport, producer: KafkaProducer[String, DroneReport]) = {
        val record = new ProducerRecord[String, DroneReport]("drone-report", droneReport.reportID.toString, droneReport)
        producer.send(record, (metadata: RecordMetadata, exception: Exception) => {
            if (exception != null) {
                exception.printStackTrace()
            } else {
                println(s"Metadata about the sent record: $metadata")
            }
        })
    }

    // lui faut trouver quand est ce qu'on l'appel et depuis ou
    def sendRecords(droneReports: List[DroneReport]) = {
        val producer = new KafkaProducer[String, DroneReport](this.props)

        droneReports.foreach { record => sendReport(record, producer) }

        producer.close()
    }
}
