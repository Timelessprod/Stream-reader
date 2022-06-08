import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.json4s.native.Serialization

import java.util.Properties
// import org.apache.spark.streaming.StreamingContext
// import scala.concurrent.duration.Seconds
// import org.apache.spark.streaming.kafka.KafkaUtils
// import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
// import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
// import org.apache.spark.sql.SparkSession


class Stream_Kafka(input: DroneReport) {
  // val sparkConf = SparkSession.builder().master("local[2]").appName("spark").getOrCreate()
  // val ssc = new StreamingContext(sparkConf, Seconds(1))
  // val kafkaParams = Map[String, Object](
  //     "bootstrap.servers" -> "localhost:9092,anotherhost:9092",
  //     "key.deserializer" -> Serialization,
  //     "value.deserializer" -> Serialization,
  //     "group.id" -> "0001",
  //     "auto.offset.reset" -> "latest",
  //     "enable.auto.commit" -> (false: java.lang.Boolean)
  // )
  // val topics = Array("drone-report")
  // val stream = KafkaUtils.createDirectStream[String, DroneReport](
  //     ssc,
  //     PreferConsistent,
  //     Subscribe[String, DroneReport](topics, kafkaParams)
  // ).map{ record => (record.key(), record.value) }

  val streamConf = new Properties()
  streamConf.put(StreamsConfig.APPLICATION_ID_CONFIG, "myapp")
  streamConf.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092")
  streamConf.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serialization)
  streamConf.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serialization)
  // Tells Kafka which offset use the first time the consumer consume this topic
  streamConf.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val builder = new StreamsBuilder()
  // Read the input Kafka topic into a KStream instance
  // val stream: KStream[String, DroneReport] = builder.stream[String, DroneReport]("drone-report")

  // Do what we want in a map
  // val doWhatWeWant

  // Write the results to a new Kafka topic called "DoWhatWeWant"
  // DoWhatWeWant.to("DoWhatWeWant")

  // start the application
  val streams = new KafkaStreams(builder.build(), streamConf)
  streams.start()

  // streams.waitUntil()


}