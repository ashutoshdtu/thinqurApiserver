name := "apiserver"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.21",
  "org.json"%"org.json"%"chargebee-1.0",
  "org.jdbi" % "jdbi" % "2.59"
)     

play.Project.playJavaSettings
