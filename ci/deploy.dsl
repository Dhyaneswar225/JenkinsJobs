import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.PropertiesFiles
import policies.sfly.SflyDefaults

for (env in Defs.envs) {
    ((DslFactory) this).freeStyleJob("${Defs.ecsName}-deploy-${env}") {
        SflyDefaults.gitDescription(delegate, "Deploy ${Defs.appName} to ECS ${env}.", Defs.repoUrl, this)

        SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

        parameters {
            stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
        }

        scm {
            SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
        }

        SflyDefaults.deployToEcs(delegate, SflyDefaults.getAwsAccount(Defs.awsAccountPrefix, env), env)

        blockOnDownstreamProjects()

        publishers {
            downstreamParameterized {
                trigger("${Defs.ecsName}-smoke-${env}") {
                    parameters {
                        currentBuild()
                        gitRevision()
                        propertiesFile(PropertiesFiles.TERRAFILE_FILE_PATH.value, true)
                    }
                }
            }

            SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
        }
    }
}
