import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.logging.log4j.{LogManager, Logger}

import java.time.Duration
import scala.collection.JavaConverters._
import java.util.Properties

object Consumer {
    lazy val logger: Logger = LogManager.getLogger(this.getClass)

    val props: Properties = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(List("topic_1").asJava)

    val content: Map[String, String] = Map()

    val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))
    records.asScala.foreach(record => {
        content + (record.key() -> record.value())
        logger.info(s"Offset: ${record.offset()} Key: ${record.key()} Value: ${record.value()}")
    })

    def run(): Unit = {
        consumer.commitSync()
    }
}
