/*<<<<<<< HEAD
import java.util
import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.logging.log4j.{LogManager, Logger}
import scala.collection.JavaConverters._
// import scala.jdk.CollectionConverters._
import java.time.Duration


object Consumer {
    lazy val logger: Logger = LogManager.getLogger(getClass.getName)
    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "0001")

    
    def receiveReport(consumer: KafkaConsumer[String, String]): Unit = {
        println("test 1")
        val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))
        println("test 2")
        records.asScala.foreach { record =>
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")}
        println("test 3")
        
        logger.info(s"${records.count()} report(s) received")
        println(s"${records.count()} report(s) received")

        receiveReport(consumer)
        println("test 4")
        
    }

    def run() = {
        val topic = "drone-report"
        val consumer = new KafkaConsumer[String, String](this.props)
        consumer.subscribe(util.Collections.singletonList(topic))

        println("Starting consuming")
        receiveReport(consumer)
        println("End of consuming")
    }

=======*/
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import io.circe._
import io.circe.parser._
import org.apache.logging.log4j.{LogManager, Logger}

import java.time.Duration
import scala.collection.JavaConverters._
import java.util.Properties
import java.util
import scala.annotation.tailrec


object Consumer {
    lazy val logger: Logger = LogManager.getLogger(this.getClass)

    val props: Properties = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group")
    
    val topic = "drone-report"
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Collections.singletonList(topic))

    // val content: Map[String, String] = Map()
    //
    // val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(10000))
    // records.asScala.foreach(record => {
    //     content + (record.key() -> record.value())
    //     println(s"Offset: ${record.offset()} Key: ${record.key()} Value: ${record.value()}")
    // })
    // logger.info("test")

    @tailrec
    def receiveReport(consumer: KafkaConsumer[String, String]): Unit = {
        val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))

        records.asScala.foreach(record => {
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
        })
        logger.info(s"${records.count()} report(s) received")

        receiveReport(consumer)
    }


    def run(): Unit = {
        receiveReport(consumer)
        consumer.commitSync()
    }
}
