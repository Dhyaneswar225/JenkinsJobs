import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults
import policies.DefaultJobOptions

((DslFactory) this).matrixJob("${Defs.appName}-config") {

    SflyDefaults.gitDescription(
        delegate,
        "Create environment specific configuration jars for ${Defs.appName}.",
        Defs.repoUrl,
        this)

    parameters {
        stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
    }

    SflyDefaults.basic(delegate)
    SflyDefaults.mvn(
        delegate,
        '*-\${Environment}.jar',
        "com/shutterfly/services/${Defs.artifactId}-config",
        'configdata/pom.xml',
        '-DscmRevision=\${scm_revision} -Dshared.cm.data=../sfly-cm/sfly_configurations/data')

    SflyDefaults.secretBindingConfig(delegate, Defs.thycoticAccount)

    SflyDefaults.gitWithSharedSflyConfig(delegate, Defs.repoUrl, '*/${Branch}')

    SflyDefaults.setupAxes(delegate, DefaultJobOptions.SLAVE.value, Defs.envs)
    configure { node -> node.remove(node / triggers) }

    blockOnDownstreamProjects()

    publishers {
        downstreamParameterized {
            trigger("${Defs.appName}-dockerize-service") {
                parameters {
                    currentBuild()
                    gitRevision()
                    predefinedProps([ Version: '\${dependency_version}', Branch: '\${Branch}' ])
                }
            }
        }

        SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
    }
}
