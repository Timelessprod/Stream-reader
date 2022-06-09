import java.io.Serializable
import ujson._

@SerialVersionUID(100L)
class SerdeDrone(value: ujson.Value) extends Serializable {
  override def toString = f"${value.toString}"
}