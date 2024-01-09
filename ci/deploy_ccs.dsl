import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults
import policies.PropertiesFiles
import Defs

for (env in Defs.envs) {
    ((DslFactory) this).freeStyleJob("${Defs.appName}-ccs-deploy-${env}") {
        description("Deploy ${Defs.appName} config to CCS ${env}. \nJob is automatically generated.")

        SflyDefaults.basic(delegate)
        configure { node -> node.remove(node / triggers) }

        SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

        parameters {
            stringParam('Version', '', 'Version of config jar to use for CCS')
            stringParam('Environment', "${env}", 'Environment of config jar to use for CCS')
            if(env == 'prod') {
                stringParam('CCP', '', 'Service Now ticket to deploy to CCS in production')
            }
        }

        SflyDefaults.CCSUploadConfig(delegate, "/com/shutterfly/services/${Defs.appName}-config")

        blockOnDownstreamProjects()

        if( env != 'stage' && env != 'prod') {
            SflyDefaults.triggerJob(delegate, "${Defs.appName}-deploy-${env}-ecs", {
                gitRevision()
                currentBuild()
                predefinedProps([ "dockerTag": "\${scm_revision}"])
            })
        } 
        publishers {
            SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
        }
    }
}
