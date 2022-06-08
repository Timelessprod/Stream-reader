import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.logging.log4j.{Logger, LogManager}


object Producer extends App {
    val props = new Properties()
    lazy val logger: Logger = LogManager.getLogger(getClass.getName)

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    def sendReport(droneReport: DroneReport, producer: KafkaProducer[String, DroneReport]) = {
        val record = new ProducerRecord[String, DroneReport]("drone-report", droneReport.reportID.toString, droneReport)
        producer.send(record, (metadata: RecordMetadata, exception: Exception) => {
            if (exception != null) {
                exception.printStackTrace()
            } else {
                logger.info(s"Metadata about the sent record: $metadata")
            }
        })
    }

    def sendRecords(droneReports: List[DroneReport]) = {
        val producer = new KafkaProducer[String, DroneReport](this.props)

        droneReports.foreach { record => sendReport(record, producer) }
        logger.info("Drone reports send")

        producer.close()
        logger.info("Producer closed")
    }
}
