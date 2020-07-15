name := "Akka_with_Cassandra_Connector"

version := "1.0"

scalaVersion := "2.12.11"

/*
 * Akka: https://doc.akka.io/docs/akka/2.3/intro/getting-started.html
 */

libraryDependencies ++= {

  val SparkVersion = "2.4.5"
  val AkkaVersion = "2.5.31"

  Seq(
    // Spark
    "org.apache.spark" %% "spark-core" % SparkVersion,
    "org.apache.spark" %% "spark-sql" % SparkVersion,
    // Akka
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    // Cassandra
    "com.datastax.cassandra" % "cassandra-driver-core" % "4.0.0",
    "com.datastax.spark" %% "spark-cassandra-connector" % "2.5.0",
//    // Other packages
    "log4j" % "log4j" % "1.2.16"
  )

}

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)
