import ujson._

import scala.io.Source
import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.logging.log4j.{LogManager, Logger}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

object Producer {
    lazy val logger: Logger = LogManager.getLogger(getClass.getName)

    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // need various brokers
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    // get up the Json and convert it in array of map of the droneReports
    // use:
    //     data : ArrayBuffer[ujson.Value]
    //     data(0) : ujson.Value
    //     data(0)("reportId") : Int (attribut reportId of the 1st report)
    def readJson(filename: String): ArrayBuffer[Value] = {
        val jsonString = Source.fromFile(filename)
        val data = ujson.read(jsonString.getLines().mkString)
        jsonString.close()
        data("reports").arr
    }

    // send ONE report into the stream
    def sendReport(droneReport: ujson.Value, producer: KafkaProducer[String, String]) = {
        val record = new ProducerRecord[String, String]("drone-report", droneReport("reportId").toString, droneReport.toString)
        producer.send(record, (metadata: RecordMetadata, exception: Exception) => {
            if (exception != null) {
                exception.printStackTrace()
            } else {
                logger.info(s"Metadata about the sent record: $metadata")
            }
        })
    }

    // send all reports from the array into the stream
    def sendReports(droneReports: ArrayBuffer[ujson.Value]) = {
        val producer = new KafkaProducer[String, String](this.props)

        droneReports.foreach { record => sendReport(record, producer) }
        logger.info("Drone reports send")

        producer.close()
        logger.info("Producer closed")
    }
    
    def run(jsonpath: String): Unit = {
        val droneReports = readJson(jsonpath)
        sendReports(droneReports)


        // val test = readJson("../test.json")
        // sendReports(test)
    }
}
