name := """apiserver"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "be.objectify"  %% "deadbolt-java"     % "2.3.0-RC1",
  "com.feth"      %% "play-authenticate" % "0.6.5-SNAPSHOT",
  "org.mongodb" % "mongo-java-driver" % "2.13.0",
  "org.mongodb.morphia" % "morphia" % "0.110",
  "org.mongodb.morphia" % "morphia-logging-slf4j" % "0.110",
  "org.mongodb.morphia" % "morphia-validation" % "0.110",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.5.1",
  "org.springframework" % "spring-context" % "4.1.1.RELEASE",
  "org.springframework" % "spring-orm" % "4.1.1.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.1.1.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.1.RELEASE",
  "org.springframework" % "spring-aop" % "4.1.1.RELEASE",
  "org.springframework" % "spring-test" % "4.1.1.RELEASE" % "test",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)


resolvers ++= Seq(
  "Apache" at "http://repo1.maven.org/maven2/",
  "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/",
  "play-easymail (release)" at "http://joscha.github.io/play-easymail/repo/releases/",
  "play-easymail (snapshot)" at "http://joscha.github.io/play-easymail/repo/snapshots/",
  Resolver.url("Objectify Play Repository", url("http://schaloner.github.io/releases/"))(Resolver.ivyStylePatterns),
  "play-authenticate (release)" at "http://joscha.github.io/play-authenticate/repo/releases/",
  "play-authenticate (snapshot)" at "http://joscha.github.io/play-authenticate/repo/snapshots/"
)