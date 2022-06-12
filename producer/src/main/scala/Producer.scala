import ujson._
import scala.io.Source
import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.logging.log4j.{LogManager, Logger}
import java.util
import scala.collection.mutable.ArrayBuffer

object Producer {
    lazy val logger: Logger = LogManager.getLogger(getClass.getName)

    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // need various brokers
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    /**
     * Read a JSON file ans extracts the reports from it
     * @param filename: Path to the JSON file
     * @return A list of reports as JSON objects
     */
    def readJson(filename: String): ArrayBuffer[Value] = {
        val jsonString = Source.fromFile(filename)
        val data = ujson.read(jsonString.getLines().mkString)
        jsonString.close()
        data("reports").arr
    }

    /**
     * Send a JSON object as string to the Kafka stream on topic 'drone-reports'
     * @param droneReport: JSON object representing a drone report
     * @param producer: The Kafka stream producer
     */
    def sendReport(droneReport: ujson.Value, producer: KafkaProducer[String, String]): util.concurrent.Future[RecordMetadata] = {
        val record = new ProducerRecord[String, String]("drone-report", droneReport("reportId").toString, droneReport.toString)
        producer.send(record)
    }

    /**
     * Read JSON and send each object one by one to the stream
     * @param droneReports: List of JSON objects representing the drone reports
     */
    def sendReports(droneReports: ArrayBuffer[ujson.Value]): Unit = {
        val producer = new KafkaProducer[String, String](this.props)

        droneReports.foreach { record => sendReport(record, producer) }
        logger.info("Drone reports send")

        producer.close()
        logger.info("Producer closed")
    }

    /**
     * Main methode that take a JSON file and call the methode `sendReports` to
     * send the reports to the Kafka stream
     * @param jsonpath: Path to the JSON file
     */
    def run(jsonpath: String): Unit = {
        val droneReports = readJson(jsonpath)
        sendReports(droneReports)
    }
}
