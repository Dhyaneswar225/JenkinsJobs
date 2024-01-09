package com.shutterfly.loadtest.example.services

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

object SpringboardExampleService {

  val REQUEST_ALL_USERS = "Get all users"
  val REQUEST_USER_BY_ID = "Get user by Id"

  def retrieveUsers(): ChainBuilder = {
    exec(http(REQUEST_ALL_USERS)
           .get("/users")
           .check(jsonPath("$[0]['userID']").ofType[String].saveAs("userId"))
           .check(status.is(200))
    )
  }

  def retrieveUserById() : ChainBuilder = {
    exec(http(REQUEST_USER_BY_ID)
           .get("/users/${userId}")
           .check(jsonPath("$.userID").is("${userId}"))
           .check(status.is(200))
    )
  }
}
