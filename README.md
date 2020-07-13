# AKKA-with-Cassandra

### Description
It's a sample code which build connector of database Cassandra with datastax driver.
<br>

### Motivation
Study and learn about database. For decentralized system, it must be Cassandra.
<br>

### Skills
Language: Scala <br>
Version: 2.12 <br>
Framework: Spark (version: 2.4.5), AKKA (version: 2.4.20) <br>
Databsase: Cassandra (Datastax driver-core version: 3.6.0, Spark connector version: 2.5.0) <br>

#### Environment
OS: MacOS (Current Version: 10.14.5)
<br>

### Pre-Process 
It's necessary that install Cassandra in environment (Or current environment could connect to database) before start this project. <br>

Start database. <br>

    cassandra

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/start-cassandra-console.png)
<br>

Access to Cassandra console after ensure the service activates successfully without any issue. <br>

    cqlsh
    
Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/start-cassandra-cql-console.png)
<br>

cqlsh is a shell for interacting with Cassandra through Cassandra Query Language (a.k.a CQL). <br>
It supports feature about checking which keyword could use at next step and auto-complete by tab. <br>

Here are some basic element of Cassandra: <br>
* Keyspace <br>
Here is the explanation from document: <br>
ps Cassandra Official document URL: <https://www.tutorialspoint.com/cassandra/index.htm> <br>
> A keyspace in Cassandra is a namespace that defines data replication on nodes. A cluster contains one keyspace per node. Given below is the syntax for creating a keyspace using the statement CREATE KEYSPACE. <br>

In other words, a keyspace be composed of multiple tables. <br>

* Table <br>
Just the general table which be know. <br>

Show all Keyspaces: <br>

    DESCRIBE KEYSPACES

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/cqlsh/show-all-keyspace.png)
<br>

Go into one keyspace. <br>

    use test_keyspace ;

It could create keyspace and define some attributes by develop-self

    CREATE KEYSPACE <keyspace name> WITH replication = {'class': <replication strage type>, 'replication_factor' : <replication amount>};
    
For parameter 'replication strage type', please refer to the document <https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cqlCreateKeyspace.html>. <br>

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/cqlsh/show-all-keyspace.png)
<br>

Show all tables in the keyspace. <br>

    DESCRIBE TABLES

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/cqlsh/show-all-tables.png)
<br>

Create table: <br>

    CREATE TABLE <table name> ( <column name> <column data type> (Primary KEY) );

For example, cql shell for this project to create table is <br>

    CREATE TABLE testWithAkka ( index1 int Primary KEY , index2 int, index3 int , index4 int , index5 int );

By the way, the option 'PRIMARY KEY' also could be set like list. So the cql shell could be written as 

    CREATE TABLE testWithAkka ( index1 int, index2 int, index3 int , index4 int , index5 int , PRIMARY KEY (index1) );

Finish all above, of cource could do some basic database operators: INSERT, UPDATE and DELETE. <br>

INSERT
---
Add data into one specific table. <br>

    INSERT INTO <table name> (<columns name>) VALUES (<values which mapping columns>);

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/cqlsh/cqlsh_insert_cmd_result.png)
<br>

UPDATE
---
Update data of target table. <br>

    UPDATE <table name> SET <be updated column> <operator> <new value> WHERE <column (Primary key)> <operator> <new value>;

In the lab, the CQL shell is <br>

    UPDATE testwithakka SET index2 = 9999 WHERE index1 = 77;

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/cqlsh/cqlsh_update_cmd_result_before_run.png)
<br>

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/cqlsh/cqlsh_update_cmd_result_after_run.png)
<br>

DELETE
---
Remove data from target table. <br>

    DELETE FROM <table name> WHERE <column (Primary key)> <operator> <new value>;

In the lab, the CQL shell is <br>

    DELETE FROM testwithakka WHERE index1 = 3;

<br>


### Build connector with API
It need to save data by Spark with datastax driver. <br>
Build a SparkContext first, <br>

```scala
// Build a Spark interface connector with database Cassandra
// Build and set Spark configuration
val conf = new SparkConf(true)
  .set("spark.cassandra.connection.host", "127.0.0.1")     // Connect to database Cassandra
  .setMaster("local[*]")
  .setAppName("CassandraConnector")
// Apply configuration and build Spark 
val sc = new SparkContext(conf)
```

Call method 'saveToCassandra' to write data into database. <br>
```scala
val scDataframe = sc.parallelize(Seq(data))
scDataframe.saveToCassandra(keyspace, table, SomeColumns("column1", "column2", "column3", "column4", "column5"))
```

It has another way to save data into database. <br>


### Running Result
Here is some parts of log message when running the project program: <br>

* Spark log: <br>

2020-07-08 13:24:19 INFO  SparkContext:54 - Running Spark version 2.4.5  <br>
2020-07-08 13:24:20 WARN  NativeCodeLoader:62 - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable  <br>
2020-07-08 13:24:20 INFO  SparkContext:54 - Submitted application: CassandraConnector  <br>
2020-07-08 13:24:20 INFO  SecurityManager:54 - Changing view acls to: bryantliu  <br>
2020-07-08 13:24:20 INFO  SecurityManager:54 - Changing modify acls to: bryantliu  <br>
2020-07-08 13:24:20 INFO  SecurityManager:54 - Changing view acls groups to:   <br>
2020-07-08 13:24:20 INFO  SecurityManager:54 - Changing modify acls groups to:   <br>
2020-07-08 13:24:20 INFO  SecurityManager:54 - SecurityManager: authentication disabled; ui acls disabled; users  with view permissions: Set(bryantliu); groups with view permissions: Set(); users  with modify permissions: Set(bryantliu); groups with modify permissions: Set()  <br>
2020-07-08 13:24:20 INFO  Utils:54 - Successfully started service 'sparkDriver' on port 56879.  <br>
<br>

* AKKA Code log:  <br>

[INFO] [07/08/2020 13:24:21.618] [AkkaWithCassandra-akka.actor.default-dispatcher-4] [akka://AkkaWithCassandra/user/DatabaseKing] Will prepare to save data to database.  <br>
[INFO] [07/08/2020 13:24:21.627] [AkkaWithCassandra-akka.actor.default-dispatcher-8] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-0] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.627] [AkkaWithCassandra-akka.actor.default-dispatcher-7] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-1] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.627] [AkkaWithCassandra-akka.actor.default-dispatcher-6] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-2] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.627] [AkkaWithCassandra-akka.actor.default-dispatcher-5] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-3] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.627] [AkkaWithCassandra-akka.actor.default-dispatcher-2] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-4] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.627] [AkkaWithCassandra-akka.actor.default-dispatcher-9] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-5] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.627] [AkkaWithCassandra-akka.actor.default-dispatcher-3] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-7] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.628] [AkkaWithCassandra-akka.actor.default-dispatcher-10] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-6] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.628] [AkkaWithCassandra-akka.actor.default-dispatcher-12] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-8] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.628] [AkkaWithCassandra-akka.actor.default-dispatcher-15] [akka://AkkaWithCassandra/user/DatabaseKing/data-soldier-9] Receive task!  <br>
[INFO] [07/08/2020 13:24:21.628] [AkkaWithCassandra-akka.actor.default-dispatcher-11] [akka://AkkaWithCassandra/user/DatabaseKing] Got data. Thank you for your help!  <br>
......  <br>
[INFO] [07/08/2020 13:24:26.030] [AkkaWithCassandra-akka.actor.default-dispatcher-11] [akka://AkkaWithCassandra/user/DatabaseKing] Got data. Thank you for your help!  <br>
......  <br>
2020-07-08 13:24:27 INFO  DAGScheduler:54 - Job 9 finished: runJob at RDDFunctions.scala:36, took 0.046523 s  <br>
[INFO] [07/08/2020 13:24:27.518] [AkkaWithCassandra-akka.actor.default-dispatcher-11] [akka://AkkaWithCassandra/user/DatabaseKing] Finish all job!  <br>
<br>

Verify the data in database: <br>

    SELECT * from testwithakka ;

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Cassandra/raw/master/docs/imgs/cqlsh/show-data-with-table.png)
<br>

The data is the result after running several times. The latest data is columns 'index2', 'index3', 'index4' and 'index5' are values '20', '11', '11' and '11'. <br>

