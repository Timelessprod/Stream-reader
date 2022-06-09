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

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    // props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "SerdeDrone")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    // récupère le json sous forme d'array de map des droneReports
    // utilisation:
    //     data : ArrayBuffer[ujson.Value]
    //     data(0) : ujson.Value
    //     data(0)("reportId") : Int (attribut reportId du premier report)
    def readJson(filename: String): ArrayBuffer[Value] = {
        val jsonString = Source.fromFile(filename)
        val data = ujson.read(jsonString.getLines().mkString)
        jsonString.close()
        data("reports").arr
    }

    // envoie UN report dans la stream
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

    // envoie tous les reports dans l'array dans la stream
    def sendReports(droneReports: ArrayBuffer[ujson.Value]) = {
        val producer = new KafkaProducer[String, String](this.props)

        droneReports.foreach { record => sendReport(record, producer) }
        logger.info("Drone reports send")

        producer.close()
        logger.info("Producer closed")
    }
    
    def run(): Unit = {
        // je sais pas comment ils sont rangés et créer là donc faudra vérifier
        val droneReports = readJson("../json/s1.json")
        // sendReports(droneReports)


        println("test is testing")
        val test = readJson("../test.json")
        println(test.getClass.getName)
        println(test(0).getClass.getName)
        println(test(0).toString)
        sendReports(test)/**/
        println("I did it ?!")
    }
}
