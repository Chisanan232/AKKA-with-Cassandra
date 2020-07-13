package Akka_with_Cassandra_Connector.src.main.scala

import akka.actor.{ActorRef, ActorSystem, Props}


object DatabaseMain extends App {

  /***
   * Save data to database with Spark.
   */
  val system = ActorSystem("AkkaWithCassandra")
  val king: ActorRef = system.actorOf(Props[DatabaseKing], "DatabaseKing")
  king ! SaveData

}


object SaveDataToCassandraBySpark extends App {

  /***
   * Convert data to dataframe and save it to database directly with Spark.
   */
  val ss = new SparkSaver
  ss.saveData()
  ss.closeSession()

}

