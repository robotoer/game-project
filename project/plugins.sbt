resolvers += Resolver.url("scalasbt releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

scalaVersion := "2.9.1"

addSbtPlugin("org.scala-sbt" %% "sbt-android-plugin" % "0.6.1")
