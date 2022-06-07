import java.util
import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
// import org.json4s.native.Serialization
import scala.collection.JavaConverters._
import java.time.Duration


object Consumer extends App {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
    // props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serialization)
    // props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serialization)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "0001")
    
    def receiveReport(): Unit = {
        val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))   // ca c'est pour la pate chaude (la pate froide c'est plutot 15min)
        records.asScala.foreach { record =>
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")

        receiveReport()
        }
    }

    val topic = "drone-report"
    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Collections.singletonList(topic))

    receiveReport()

}
