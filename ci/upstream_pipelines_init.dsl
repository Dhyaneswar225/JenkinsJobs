import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults

def upstreamEnvs = Defs.envs - Defs.initialDeploymentEnv

for (env in upstreamEnvs) {
    if (env != 'prod') {
        ((DslFactory) this).freeStyleJob("${Defs.appName}-service-${env}-pipeline-init") {

            if (env == 'beta') {
                triggers {
                    cron('@daily')
                }
            }

            SflyDefaults.gitDescription(
                delegate,
                "Initialize ${Defs.appName} ${env} pipeline.",
                Defs.repoUrl,
                this)

            SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

            SflyDefaults.ecsPipelineInit(
                delegate,
                "${Defs.ecsName}-smoke-${Defs.initialDeploymentEnv}",
                "${Defs.appName}-localIT-${env}")

            publishers {
                SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
            }
        }
    }
}
