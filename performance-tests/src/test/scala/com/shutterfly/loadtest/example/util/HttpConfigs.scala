package com.shutterfly.loadtest.example.util

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocol
import io.gatling.http.protocol.HttpProtocolBuilder

object HttpConfigs {

  def newSpringboardExampleServiceConfig(
                                   baseURL: String = Configuration.exampleserver.httpUrl,
                                   cachingEnabled: Boolean = false,
                                   bypassLocalCache: String = "NONE", // NONE, READ, READ_WRITE
                                   maxRedirects: Int = 5): HttpProtocolBuilder = {

    val httpConf: HttpProtocolBuilder = http
      .baseURL(baseURL)
      .header("SFLY-apiKey", Configuration.apigeeApiKey)
      .header("SFLY-clientId", "LNPTest")
      .header("SFLY-bypassCache", bypassLocalCache)
      .maxRedirects(maxRedirects)

    if (cachingEnabled) httpConf else httpConf.disableCaching
  }
}


