package com.shutterfly.loadtest.example.simulations

import com.shutterfly.test.load.framework.util.StandardDurations
import com.shutterfly.loadtest.example.services.SpringboardExampleService
import io.gatling.core.scenario.Simulation
import com.shutterfly.loadtest.example.util.{HttpConfigs, Configuration}
import io.gatling.core.Predef._

class SpringboardExampleSimulation extends Simulation {

  // Set default number of concurrent users
  val users = Configuration.getNumUsers(1)
  val httpConf = HttpConfigs.newSpringboardExampleServiceConfig(Configuration.exampleserver.httpUrl)

  val retrieveUserScenario = scenario("Retrieve User")
    .during(StandardDurations.MEDIUM) {
                                        exec(SpringboardExampleService.retrieveUsers())
                                          .exec(SpringboardExampleService.retrieveUserById())
                                      }
  // Assertion numbers should be decided after running the test on Jenkins and analyzing the data
  setUp(retrieveUserScenario.inject(rampUsers(users) during 20))
  .protocols(httpConf)
  .assertions(details(SpringboardExampleService.REQUEST_ALL_USERS).requestsPerSec.gt(14000),
              details(SpringboardExampleService.REQUEST_ALL_USERS).responseTime.percentile3.lt(10),
              details(SpringboardExampleService.REQUEST_ALL_USERS).failedRequests.percent.lt(0),
              details(SpringboardExampleService.REQUEST_USER_BY_ID).requestsPerSec.gt(14000),
              details(SpringboardExampleService.REQUEST_USER_BY_ID).responseTime.percentile3.lt(10),
              details(SpringboardExampleService.REQUEST_USER_BY_ID).failedRequests.percent.lt(0))
}
