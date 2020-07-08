package Akka_with_Cassandra_Connector.src.main.scala

import akka.actor.{Actor, ActorLogging}


class DatabaseSoldier extends Actor with ActorLogging {

  private def generateData(soldierID: Int): (Int, Int, Int, Int, Int) = {
    (11 * soldierID, 20, 11, 11, 11)
  }

  override def receive: Receive = {

    case GetData =>
      log.info("Receive task!")
      val soldierID = self.path.name.toString.takeRight(1).toInt
      sender() ! SaveDataToCassandra("Here is the data I got! Please save it to database.", this.generateData(soldierID))

  }

}
