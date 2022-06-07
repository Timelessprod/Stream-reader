object Producer {
    // Producer
    def sendReport(droneReport: DroneReport, producer: KafkaProducer[String, DroneReport]) = {
        val record = new ProducerRecord[String, DroneReport]("drone-report", droneReport.reportID.toString, droneReport)
        producer.send(record)
    }

    def sendRecords(droneReports: List[DroneReport]) = {
        val props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serialization)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serialization)
        val producer = new KafkaProducer[String, DroneReport](props)

        droneReports.foreach { record => sendReport(record, producer) }

        producer.close()
    }
}
