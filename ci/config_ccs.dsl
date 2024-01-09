import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults
import policies.DefaultJobOptions
import Defs

((DslFactory) this).matrixJob("${Defs.appName}-ccs-config") {

    description("Create environment specific CCS configuration jars for ${Defs.appName}.\nAutomatically generated from ci/...")

    SflyDefaults.basic(delegate)
    SflyDefaults.mvn(delegate, '*-\${Environment}.jar', "com/shutterfly/services/${Defs.appName}-config", 'configdata/pom.xml', '-DscmRevision=\${scm_revision} -Dshared.cm.data=../sfly-cm/sfly_configurations/data')
    SflyDefaults.gitWithSharedSflyConfig(delegate, Defs.repoUrl, '*/${Branch}')
    SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

    parameters {
        stringParam('ccs-config', 'true', 'Property to activate the Springboard configdata nosecret profile')
    }

    configure { node -> node.remove(node / triggers) }


    SflyDefaults.setupAxes(delegate, DefaultJobOptions.SLAVE.value, Defs.envs, "dev")

    publishers {
        SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
    }
}