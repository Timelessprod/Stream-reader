case class DroneReport(
  reportID: Int,
  droneID: Int,
  time: Timestamp,
  longitude: Double,
  latitude: Double,
  heardWords: Array[String],
  peaceScores: map[Int, Int]
)

case class Id[Ressource](value: String) extends AnyVal