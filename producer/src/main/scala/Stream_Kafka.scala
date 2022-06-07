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
  // // Producer
  // def sendReport(droneReport: DroneReport, producer: KafkaProducer[String, DroneReport]) = {
  //     val record = new ProducerRecord[String, DroneReport]("drone-report", droneReport.reportID.toString, droneReport)
  //     producer.send(record)
  // }

  // def sendRecords(droneReports: List[DroneReport]) = {
  //     val props = new Properties()
  //     props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
  //     props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serialization)
  //     props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serialization)
  //     val producer = new KafkaProducer[String, DroneReport](props)

  //     droneReports.foreach { record => sendReport(record, producer) }

  //     producer.close()
  // }

  // // Consumer
  // def receiveReport() = {
  //     val props = new Properties()
  //     props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
  //     props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serialization)
  //     props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serialization)
  //     props.put(ConsumerConfig.GROUP_ID_CONFIG, "0001")
  //     val consumer = new KafkaConsumer[String, DroneReport](props)
  //     consumer.subscribe(List("drone-report").asJava)

  //     val records: ConsumerRecords[String, DroneReport] = consumer.poll(Duration.ofMillis(100))   // ca c'est pour la pate chaude (la pate froide c'est plutot 15min)
  //     records.asScala.foreach { record =>
  //         println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
  //     }
  // }

  // balance les droneReports dans la stream
  // val droneRepStream = builder.stream[Id[DroneReport], DroneReport]("drone-report")

  // etapes pour envoyer un report (a commenter car la ca le fait 1 fois pour l'exemple)
  // val droneRep = DroneReport(123456789, 987654321, "2016-01-01T00:00:00.000Z", 0.0, 0.0, Array("Hello", "I", "am", "a", "PeaceWatcher"), Map(Array(147852369, 10)))

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