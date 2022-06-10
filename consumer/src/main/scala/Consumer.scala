import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import io.circe._
import io.circe.parser._
import org.apache.logging.log4j.{LogManager, Logger}
import org.apache.kafka.common.TopicPartition

import java.time.Duration
import scala.collection.JavaConverters._
import java.util.Properties
import java.util
import scala.annotation.tailrec
import org.json4s.JString
// import org.apache.spark.sql.SparkSession


object Consumer {
    // logger
    lazy val logger: Logger = LogManager.getLogger(this.getClass)

    // spark
    // val spark = SparkSession.builder().appName("Consumer").getOrCreate()
    // import spark.implicits._

    // config for the consumer
    val props: Properties = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group")
    
    val topic = "drone-report"
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Collections.singletonList(topic))

    // val content: Map[String, String] = Map()

    // infinite loop to get reports from the stream
    @tailrec
    def receiveReport(consumer: KafkaConsumer[String, String]): Unit = {
        val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))

        records.asScala.foreach(record => {
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
            val recordString = record.value().toString
            println(recordString)
            val recordJson = JString(recordString)
            println(recordJson)
            // val df = spark.read.json(Seq(recordString).toDS)
            // println(df)
        })
        logger.info(s"${records.count()} report(s) received")

        consumer.commitSync()

        receiveReport(consumer)
    }

    def run(): Unit = {
        logger.info("Runnig Consumer")
        receiveReport(this.consumer)
    }
}
