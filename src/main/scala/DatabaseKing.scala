package Akka_with_Cassandra_Connector.src.main.scala

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.apache.spark.{SparkConf, SparkContext}
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector._


class DatabaseKing extends Actor with ActorLogging {

  import context.dispatcher

  var currentTaskNum = 0

  // Build a Spark interface connector with database Cassandra
  val conf = new SparkConf(true)
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .setMaster("local[*]")
    .setAppName("CassandraConnector")
  val sc = new SparkContext(conf)

  // Build connector session with database Cassandra
  // https://stackoverflow.com/questions/28563809/delete-from-cassandra-table-in-spark
  val connector = CassandraConnector.apply(this.sc.getConf)
  val session = connector.openSession()

  def writeOneData(keyspace: String, table: String, data: (Int, Int, Int, Int, Int)): Unit = {
    val scDataframe = this.sc.parallelize(Seq(data))
    scDataframe.saveToCassandra(keyspace, table, SomeColumns("index1", "index2", "index3", "index4", "index5"))
  }


  def writeMultiData(keyspace: String, table: String, data: List[(Int, Int, Int, Int, Int)]): Unit = {
    /***
     * How to do some operators with Seq type data
     * https://alvinalexander.com/scala/seq-class-methods-examples-syntax/
     */
    var s: Seq[(Int, Int, Int, Int, Int)] = Nil
    data.foreach(d => {
      s = s :+ d
      // Colon site is the original Seq type data and plus site is new data. From Scala official documentation, there is
      // another operator '+:'
//      s = d +: s
    })
    val scDataframe = this.sc.parallelize(s)
    scDataframe.saveToCassandra(keyspace, table, SomeColumns("index1", "index2", "index3", "index4", "index5"))
  }


  def writeDataWithSession(CASSANDRA_SCHEMA: String, table: String, data: (Int, Int, Int, Int, Int)): Unit = {
    val insert = "INSERT INTO " + CASSANDRA_SCHEMA + "." + table + " (index1 , index2 , index3 , index4 , index5) VALUES " + data + ";"
    this.session.execute(insert)
    this.session.close()
  }


  override def receive: Receive = {

    case SaveData =>
      log.info("Will prepare to save data to database.")

      val dataSoldiers = new Array[ActorRef](10)
      for (soldierID <- 0.until(10)) dataSoldiers(soldierID) = context.actorOf(Props[DatabaseSoldier], s"data-soldier-$soldierID")
      dataSoldiers.foreach(soldierRef => {
        val soldier = context.actorSelection(soldierRef.path)
        soldier ! GetData
      })


    case SaveDataToCassandra(content, data) =>
      log.info("Got data. Thank you for your help!")
      this.writeOneData("test_keyspace", "testwithakka", data)
      // Or call this method to save data
//      this.writeDataWithSession("test_keyspace", "testwithakka", data)
      this.currentTaskNum += 1
      if (this.currentTaskNum.equals(10)) {
        log.info("Finish all job!")
        // Remember stop to close Spark normally.
        this.sc.stop()
        context.system.terminate()
      }

  }

}

