import org.apache.kafka.streams.scala.Serdes._

class Stream_Kafka(input: DroneReport) {

    val builder: StreamsBuilder = new StreamsBuilder

    val droneID: KGroupedStreamed[ReportID, DroneID] = builder.stream[ReportID, DroneID]("drone-ID").groupByKey
    val dronePosition: KGroupedStreamed[ReportID, DronePosition] = builder.stream[ReportID, DronePosition]("drone-position").groupByKey
    val peaceScoreIDs: KGroupedStreamed[ReportID, PeaceScoreIDs] = builder.stream[ReportID, PeaceScoreIDs]("peacescore-ID").groupByKey      // diff
    val timestamp: KGroupedStreamed[ReportID, Timestamp] = builder.stream[ReportID, Timestamp]("timestamp").groupByKey


    // lui je sais pas comment faire le consumer
    // implicit def consumedFromSerde[K, V](implicit keySerde: Serde[K], valueSerde: Serde[V])


    val droneReport: KTable[ReportID, DroneReport] = droneID
        .cogroup[DroneReport]({ case (_, drone, agg) => agg.copy(drone = drone.some) })
        .cogroup[DronePosition](dronePosition, { case (_, pos, agg) => agg.copy(pos = pos.some)})
        .cogroup[PeaceScoreIDs](peaceScoreIDs, { case (_, scores, agg) => agg.copy(scores = scores.some)})      // diff
        .cogroup[Timestamp](timestamp, { case (_, time, agg) => agg.copy(time = time.some)})
        .aggregate(DroneReport.empty)













    def stream_ReportID() {
        var inputTopic = input.ReportID.value
        val stringSerde = Serdes.String()
        var builder: StreamsBuilder = new StreamsBuilder()
        builder.stream(inputTopic)
    }

    def stream_DroneID() {

    }

    def stream_Position() {
        
    }

    def stream_PeaceScoreIDs() {
        
    }

    def stream_Timestamp() {
        
    }
}