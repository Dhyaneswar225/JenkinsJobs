**Table of Contents**  
- [Springboard Archetype ECS Service](#springboard-archetype-ecs-service)
  - [Job Status](#job-status)
  - [Getting Started](#getting-started)
    - [Requirements](#requirements)
    - [Thycotic Integration for SignalFX Token](#thycotic-integration-for-signalFX-token)
    - [Build and Run](#build-and-run)
    - [Endpoints to test out](#endpoints-to-test-out)
    - [To Stop](#to-stop)
    - [Service In Action (on dev)](#service-in-action-on-dev)
- [General Documentation For Service Development](#general-documentation-for-service-development)
  - [How to create a Java Service](#how-to-create-a-java-service)
  - [How to create an Apigee API Proxy](#how-to-create-an-Apigee-API-Proxy)
  - [Deploying your Service in ECS](#deploying-your-service-in-ecs)

# Springboard Archetype ECS Service


- [Dev Build pipeline](https://build.stage.shutterfly.com/view/ecs-service-archetype-verify-ecs-dev-build-pipeline/)
- Dsl seed [![Build Status](https://build.stage.shutterfly.com/job/re-springboard-ecs-service-archetype-verify-dsl-seed/badge/icon)](https://build.stage.shutterfly.com/job/re-springboard-ecs-service-archetype-verify-dsl-seed/)
- Compile [![Build Status](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-compile/badge/icon)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-compile/)
- Config [![Build Status](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-config/badge/icon)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-config/)

| Environment | Deploy (ecs) | Smoke |
| --- | --- | --- |
| Dev | [![Build Status](https://build.stage.shutterfly.com/buildStatus/icon?job=ecs-service-archetype-verify-ecs-deploy-dev)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-deploy-dev/) | [![Build Status](https://build.stage.shutterfly.com:443/buildStatus/icon?job=ecs-service-archetype-verify-ecs-smoke-dev)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-smoke-dev/) 
| Beta | [![Build Status](https://build.stage.shutterfly.com/buildStatus/icon?job=ecs-service-archetype-verify-ecs-deploy-beta)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-deploy-beta/) | [![Build Status](https://build.stage.shutterfly.com:443/buildStatus/icon?job=ecs-service-archetype-verify-ecs-smoke-beta)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-smoke-beta/)  |
| Stage | [![Build Status](https://build.stage.shutterfly.com/buildStatus/icon?job=ecs-service-archetype-verify-ecs-deploy-stage)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-deploy-stage/) | [![Build Status](https://build.stage.shutterfly.com:443/buildStatus/icon?job=ecs-service-archetype-verify-ecs-smoke-stage)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-smoke-stage/)  | 
| Prod | [![Build Status](https://build.stage.shutterfly.com/buildStatus/icon?job=ecs-service-archetype-verify-ecs-deploy-prod)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-deploy-prod/) | [![Build Status](https://build.stage.shutterfly.com:443/buildStatus/icon?job=ecs-service-archetype-verify-ecs-smoke-prod)](https://build.stage.shutterfly.com/job/ecs-service-archetype-verify-ecs-smoke-prod/)  |



- Java 8
- Maven
- Ruby (for [configuration purposes](configdata/))


Please review [page](https://wiki.corp.shutterfly.com/pages/viewpage.action?spaceKey=sfly&title=SignalFx+Service+Integration#SignalFxServiceIntegration-ThycoticIntegrationforSignalFXToken).


To build the service, execute mvn from the root project directory.

```bash
mvn clean install
```


```bash
cd configdata
mvn
cd ..
```

Executing mvn in the configdata folder will generate a `config/` directory which contains properties
to enable dev mode (including connecting to Oracle). When launching the service from the project root
directory, the service will utilize `./config` if available on startup. Otherwise, it'll use the 
local configuration (with H2 DB).


Now, we're ready to launch the service:


```bash
java -jar  <service-name>/target/<service-name>-1.0.0-SNAPSHOT-exec.jar 
```

This will start the service on the default port of 8080 connected to the dev db.


* Example API - Fetch users. <http://localhost:8080/users>
* [Health](http://localhost:8080/_internal/health)
* [Metrics](http://localhost:8080/_internal/metrics)
* [Trace](http://localhost:8080/_internal/trace)
* [Environment](http://localhost:8080/_internal/env)
* [Hystrix Dashboard](http://localhost:8080/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8080%2F_internal%2Fhystrix.stream)
* [Swagger API Documentation](http://localhost:8080/swagger-ui.html)


To gracefully stop, ctrl-c


* [Health](https://ecs-service-archetype-verify.dev.us-east-1.sfly-aws-dev.sfly.int/_internal/health)
* [Metrics](https://ecs-service-archetype-verify.dev.us-east-1.sfly-aws-dev.sfly.int/_internal/metrics)
* [Trace](https://ecs-service-archetype-verify.dev.us-east-1.sfly-aws-dev.sfly.int/_internal/trace)
* [Environment](https://ecs-service-archetype-verify.dev.us-east-1.sfly-aws-dev.sfly.int/_internal/env)
* [Endpoints](https://ecs-service-archetype-verify.dev.us-east-1.sfly-aws-dev.sfly.int/_internal/mappings)
* [Thread Dump](https://ecs-service-archetype-verify.dev.us-east-1.sfly-aws-dev.sfly.int/_internal/dump)
* [Hystrix Dashboard](https://ecs-service-archetype-verify.dev.us-east-1.sfly-aws-dev.sfly.int/hystrix/monitor?stream=https%3A%2F%2Fecs-service-archetype-verify.dev.shutterfly.com%2F_internal%2Fhystrix.stream)

# General Documentation For Service Development


Please read the documentation [here](https://github.com/sflyinc-shutterfly/springboard-example/tree/master/springboard-example)


Please read the documentation [here](https://github.com/sflyinc-shutterfly/springboard-example/wiki/Apigee). 

> NOTE: If you plan to use the files in the /apigee directory as a starting point, remember
> to modify the `name` attribute of the `APIProxy` element in the 'API proxy configuration' (./apigee/<service-name>/apiproxy/<service-name>.xml).
> Failing to do so will result in overwriting the springboard-example proxy with a proxy to 
> your own API.

For information on how to create Spike Arrest policy and determining the rate limit please read [here](https://github.com/sflyinc-shutterfly/core-apigee/blob/master/docs/spike_arrest.md)
Once you add the AWS resources in this generated application then you should be using `aws-azure-login` then select the appropriate role.

Follow this tutorial https://wiki.corp.shutterfly.com/pages/viewpage.action?spaceKey=sfly&title=ECS+Service+Onboarding

