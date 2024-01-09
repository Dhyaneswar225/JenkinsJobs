package com.shutterfly.loadtest.example.util

object Configuration {

  val exampleserver = new ServerConfiguration(
    "ecs-service-archetype-verify.stage.us-east-1.sfly-aws-dev.sfly.int", "exampleserver.server.hostname")

  def apigeeApiKey: String =
    System.getProperty("apigee.apiKey", "jivpoBy0I9Ad8zGuiSi5pNQSVn3bcP7D")

  val PROP_OVERRIDE_NUM_USERS = "users"

  def getNumUsers(defaultNumUsers: Int): Int = {
    Option(System.getProperty(PROP_OVERRIDE_NUM_USERS)).map(_.toInt).getOrElse(defaultNumUsers)
  }
}

  class ServerConfiguration(defaultHostname: String, overriderHostnameEnvParameter: String) {
    def hostname = System.getProperty(overriderHostnameEnvParameter, defaultHostname)
    def httpUrl = "http://" + hostname
    def httpsUrl = "https://" + hostname
  }