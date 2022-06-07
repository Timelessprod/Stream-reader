import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.json4s.native.Serialization

import java.time.Duration


object Consumer {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serialization)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serialization)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "0001")
    
    def receiveReport() = {
        val consumer = new KafkaConsumer[String, DroneReport](props)
        consumer.subscribe(List("drone-report").asJava)

        val records: ConsumerRecords[String, DroneReport] = consumer.poll(Duration.ofMillis(100))   // ca c'est pour la pate chaude (la pate froide c'est plutot 15min)
        records.asScala.foreach { record =>
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
        }
    }

}
