import java.util
import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.logging.log4j.{LogManager, Logger}
import scala.collection.JavaConverters._
import java.time.Duration


object Consumer {
    lazy val logger: Logger = LogManager.getLogger(getClass.getName)
    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "0001")
    
    def receiveReport(consumer: KafkaConsumer[String, String]): Unit = {
        val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))
        records.asScala.foreach { record =>
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")

        receiveReport()
        }
    }

    def run() {
        val topic = "drone-report"
        val consumer = new KafkaConsumer[String, String](this.props)
        consumer.subscribe(util.Collections.singletonList(topic))

        receiveReport(consumer)
    }

}
