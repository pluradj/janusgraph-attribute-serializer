# README

This example shows how to create and configure custom attribute serialization in JanusGraph. Out of the box, JanusGraph handles a limited set of simple types for serialization.

This `ArrayListSerializer` uses default <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/serialization/index.html">Java object serialization</a> for the list and its contents. Keep in mind that the format is not optimized for compactness, like the <a href="http://docs.janusgraph.org/latest/schema.html#d0e986">native JanusGraph data types</a> and associated <a href="https://static.javadoc.io/org.janusgraph/janusgraph-core/0.1.1/org/janusgraph/graphdb/database/serialize/attribute/package-summary.html">serializers</a>. I recommend using the native JanusGraph data types as much as possible.


## References

* [JanusGraph Database](http://janusgraph.org)
    * [Datatype and Attribute Serializer Configuration](http://docs.janusgraph.org/latest/serializer.html)
    * [AttributeSerializer Javadoc](https://static.javadoc.io/org.janusgraph/janusgraph-core/0.1.1/org/janusgraph/core/attribute/AttributeSerializer.html)
* [janusgraph-users Google Group](https://groups.google.com/d/msg/janusgraph-users/dOpxRbi-ZQI/xWYv4T1MAgAJ)


## Prerequisites

* [Apache Maven 3.3.x](http://maven.apache.org/)
* [Java 8 Update 40+](https://www.java.com/)
* [JanusGraph 0.1.1](https://github.com/JanusGraph/janusgraph/releases/)


## Build and Install

```
mvn clean package
cp target/*.jar $JANUSGRAPH_HOME/lib/
cp -r conf/* $JANUSGRAPH_HOME/conf/
```


## Attribute Serializer Configuration

```
attributes.custom.attribute1.attribute-class=java.lang.StringBuffer
attributes.custom.attribute1.serializer-class=pluradj.janusgraph.graphdb.database.serialize.attribute.StringBufferSerializer
attributes.custom.attribute2.attribute-class=java.util.ArrayList
attributes.custom.attribute2.serializer-class=pluradj.janusgraph.graphdb.database.serialize.attribute.ArrayListSerializer
```


## Create graph from Gremlin Console

```
$ rm -rf db/ log/
$ bin/gremlin.sh

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: janusgraph.imports
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/lib/slf4j-log4j12-1.7.12.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/lib/logback-classic-1.1.2.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
14:04:02 WARN  org.apache.hadoop.util.NativeCodeLoader  - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
14:04:03 WARN    - Your hostname, pluradj-mbp-2.raleigh.ibm.com resolves to a loopback/non-reachable address: fe80:0:0:0:91cb:79f4:e966:3f85%utun1, but we couldn't find any external IP address!
plugin activated: tinkerpop.hadoop
plugin activated: tinkerpop.spark
plugin activated: tinkerpop.tinkergraph
gremlin> graph = JanusGraphFactory.open('./conf/attr-janusgraph-berkeleyje.properties')
*** StringBufferSerializer constructor
*** ArrayListSerializer constructor
==>standardjanusgraph[berkeleyje:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/./conf/../db/berkeley]
gremlin> mgmt = graph.openManagement()
==>org.janusgraph.graphdb.database.management.ManagementSystem@7100dea
gremlin> hello = mgmt.makePropertyKey('hello').dataType(StringBuffer.class).cardinality(Cardinality.SINGLE).make()
==>hello
gremlin> poi = mgmt.makePropertyKey('poi').dataType(ArrayList.class).cardinality(Cardinality.SINGLE).make()
==>poi
gremlin> mgmt.commit()
==>null
gremlin> v = graph.addVertex()
==>v[4288]
gremlin> v.property('hello', new StringBuffer('world'))
==>vp[hello->world]
gremlin> l = [] as ArrayList
gremlin> l.add('bla')
==>true
gremlin> v.property('poi', l)
==>vp[poi->[bla]]
gremlin> graph.tx().commit()
*** StringBufferSerializer write
*** ArrayListSerializer write
==>null
gremlin> g = graph.traversal()
==>graphtraversalsource[standardjanusgraph[berkeleyje:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/./conf/../db/berkeley], standard]
gremlin> vx = g.V().next()
14:05:30 WARN  org.janusgraph.graphdb.transaction.StandardJanusGraphTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
==>v[4288]
gremlin> sb = vx.values('hello').next()
*** StringBufferSerializer read
==>world
gremlin> sb.getClass()
==>class java.lang.StringBuffer
gremlin> al = vx.values('poi').next()
*** ArrayListSerializer read
==>bla
gremlin> al.getClass()
==>class java.util.ArrayList
gremlin> :q
```


## Host graph on Gremlin Server

```
$ bin/gremlin-server.sh conf/gremlin-server/attr-gremlin-server.yaml
+ '[' conf/gremlin-server/attr-gremlin-server.yaml = -i ']'
+ ARGS=conf/gremlin-server/attr-gremlin-server.yaml
+ '[' 1 = 0 ']'
+ exec /opt/jdk1.8.0_144/bin/java -server -Djanusgraph.logdir=/tmp/janusgraph-0.1.1-hadoop2/bin/../log -Dlog4j.configuration=conf/gremlin-server/log4j-server.properties -Xms32m -Xmx512m -javaagent:/tmp/janusgraph-0.1.1-hadoop2/lib/jamm-0.3.0.jar -cp <classpath> org.apache.tinkerpop.gremlin.server.GremlinServer conf/gremlin-server/attr-gremlin-server.yaml
fined.
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/lib/slf4j-log4j12-1.7.12.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/lib/logback-classic-1.1.2.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
0    [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - 
         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----

114  [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Configuring Gremlin Server from conf/gremlin-server/attr-gremlin-server.yaml
181  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics ConsoleReporter configured with report interval=180000ms
183  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics CsvReporter configured with report interval=180000ms to fileName=/tmp/gremlin-server-metrics.csv
242  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics JmxReporter configured with domain= and agentId=
244  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics Slf4jReporter configured with interval=180000ms and loggerName=org.apache.tinkerpop.gremlin.server.Settings$Slf4jReporterMetrics
863  [main] INFO  org.janusgraph.graphdb.configuration.GraphDatabaseConfiguration  - Generated unique-instance-id=091b601c14025-pluradj-mbp-2-raleigh-ibm-com1
891  [main] INFO  org.janusgraph.diskstorage.Backend  - Initiated backend operations thread pool of size 16
*** StringBufferSerializer constructor
*** ArrayListSerializer constructor
957  [main] INFO  org.janusgraph.diskstorage.log.kcvs.KCVSLog  - Loaded unidentified ReadMarker start time 2017-09-13T18:07:34.469Z into org.janusgraph.diskstorage.log.kcvs.KCVSLog$MessagePuller@a87f8ec
957  [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Graph [graph] was successfully configured via [conf/gremlin-server/attr-janusgraph-berkeleyje-server.properties].
957  [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - Initialized Gremlin thread pool.  Threads in pool named with pattern gremlin-*
1332 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.ScriptEngines  - Loaded gremlin-groovy ScriptEngine
1822 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor  - Initialized gremlin-groovy ScriptEngine with scripts/empty-sample.groovy
1822 [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - Initialized GremlinExecutor and configured ScriptEngines.
1824 [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - A GraphTraversalSource is now bound to [g] with graphtraversalsource[standardjanusgraph[berkeleyje:db/berkeley], standard]
1840 [main] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the standard OpProcessor.
1841 [main] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the control OpProcessor.
1843 [main] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the session OpProcessor.
2021 [main] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the traversal OpProcessor.
2142 [main] INFO  org.apache.tinkerpop.gremlin.server.op.traversal.TraversalOpProcessor  - Initialized cache for TraversalOpProcessor with size 1000 and expiration time of 600000 ms
2161 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Executing start up LifeCycleHook
2170 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Executed once at startup of Gremlin Server.
2267 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
2269 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo-lite with org.apache.tinkerpop.gremlin.driver.ser.GryoLiteMessageSerializerV1d0
2270 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo-stringd with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
2274 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerGremlinV1d0
2288 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v2.0+json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerGremlinV2d0
2288 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerV1d0
2365 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Gremlin Server configured with worker thread pool of 1, gremlin pool of 8 and boss thread pool of 1.
2366 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Channel started at port 8182.
```


## Connect to Gremlin Server from Gremlin Console

```
$ bin/gremlin.sh

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: janusgraph.imports
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/lib/slf4j-log4j12-1.7.12.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/Users/pluradj/src/github/pluradj/janusgraph-attribute-serializer/janusgraph-0.1.1-hadoop2/lib/logback-classic-1.1.2.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
14:07:55 WARN  org.apache.hadoop.util.NativeCodeLoader  - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
14:07:56 WARN    - Your hostname, pluradj-mbp-2.raleigh.ibm.com resolves to a loopback/non-reachable address: fe80:0:0:0:91cb:79f4:e966:3f85%utun1, but we couldn't find any external IP address!
plugin activated: tinkerpop.hadoop
plugin activated: tinkerpop.spark
plugin activated: tinkerpop.tinkergraph
gremlin> :remote connect tinkerpop.server ./conf/remote.yaml
==>Configured localhost/127.0.0.1:8182
gremlin> :> g.V().properties()
==>vp[hello->world]
==>vp[poi->[bla]]
gremlin> :> g.V().values('poi')
==>[bla]
gremlin> :> g.V().values('poi').next()
==>bla
gremlin> :remote close
==>Removed - Gremlin Server - [localhost/127.0.0.1:8182]
gremlin> :q
```


## Gremlin Server output from Gremlin Console session

```
38853 [gremlin-server-exec-1] WARN  org.janusgraph.graphdb.transaction.StandardJanusGraphTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** StringBufferSerializer read
*** ArrayListSerializer read
53068 [gremlin-server-exec-2] WARN  org.janusgraph.graphdb.transaction.StandardJanusGraphTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** ArrayListSerializer read
69749 [gremlin-server-exec-3] WARN  org.janusgraph.graphdb.transaction.StandardJanusGraphTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** ArrayListSerializer read
```
