import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults

for (env in SflyDefaults.getEnvs()) {

    ((DslFactory) this).freeStyleJob("${Defs.ecsName}-deploy-canary-${env}") {
        SflyDefaults.gitDescription(delegate, "Deploy canary ${Defs.appName} to ECS ${env}.", Defs.repoUrl, this)

        SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

        parameters {
            stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
        }

        scm {
            SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
        }

        SflyDefaults.deployCanaryToEcs(delegate, "${env}", Defs.canaryCount)

        publishers {
            SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
            downstreamParameterized {
                trigger("${Defs.ecsName}-smoke-${env}") {
                    parameters {
                        currentBuild()
                    }
                }
            }
        }
    }
}
