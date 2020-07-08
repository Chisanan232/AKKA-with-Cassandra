package Akka_with_Cassandra_Connector.src.main.scala

protected trait Msg {
  val content: String
}


final case class SaveData(content: String) extends Msg
final case class GetData(content: String) extends Msg
final case class SaveDataToCassandra(content: String, data: (Int, Int, Int, Int, Int)) extends Msg
