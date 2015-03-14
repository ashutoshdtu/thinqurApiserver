name := "apiserver"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaJpa,
  "mysql" % "mysql-connector-java" % "5.1.21",
  "org.json"%"org.json"%"chargebee-1.0",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "org.hibernate" % "hibernate-core" % "3.5.6-Final",
  "org.hibernate" % "hibernate-annotations" % "3.5.6-Final",
  "joda-time" % "joda-time-hibernate" % "1.3",
  "org.jdbi" % "jdbi" % "2.59"
)     

play.Project.playJavaSettings
