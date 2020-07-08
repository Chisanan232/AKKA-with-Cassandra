package Akka_with_Cassandra_Connector.src.main.scala

import org.apache.spark.sql.SparkSession


class SparkSaver {

  val spark = SparkSession.builder()
    .appName("Save data to cassandra via Spark")
    .master("local[*]")
    .getOrCreate()


  def saveData(): Unit = {

    // Save data to database Cassandra methods
    // https://stackoverflow.com/questions/41248269/inserting-data-into-cassandra-table-using-spark-dataframe

    // A sample Json type string-data.
//    val JsonData = """{"id": "b9dec3d4-4a62-4d83-90d9-41bdd70b4c72", "createdAt": 1591825407.7899938, "city": "台北", "dist": "大安區", "tags": []}""".stripMargin
    // Or also could get data via Spark
    val jsonData = spark.read.json("The Json type data file path")
    // Show what data in it
    jsonData.show()
    // Save it to database
    jsonData.toDF().write.format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "test_keyspace", "table" -> "testcafe")).save()
  }


  def closeSession(): Unit = {
    this.spark.close()
  }

}

