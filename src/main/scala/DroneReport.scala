package object DroneReport {

  case class ReportID(value: Int)
  case class DroneID(value: Int)
  case class DronePosition(longitude: Double, latitude: Double)
  case class PeaceScoreIDs(value: map[CitizenID, Int])  // lui je sais pas encore comment le mettre dans la stream  // diff
  case class Timestamp(value: String)

}

case class DroneReport(drone: Option[DroneID], pos: Option[DronePosition], scores: Option[PeaceScoreIDs], time: Option[Timestamp])  // diff

object DroneReport {
  val empty: DroneReport = DroneReport(None, None, None, None)    // diff
}