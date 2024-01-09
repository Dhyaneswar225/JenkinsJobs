import javaposse.jobdsl.dsl.DslFactory
import policies.PropertiesFiles
import policies.sfly.SflyDefaults

import static Defs

((DslFactory) this).mavenJob("${Defs.appName}-compile") {
    SflyDefaults.gitDescription(delegate, "Compile ${Defs.appName} project", Defs.repoUrl, this)

    parameters {
        stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
    }

    scm {
        SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
    }

    // use standard Shutterfly project and maven configurations
    SflyDefaults.basicCompile(delegate)
    SflyDefaults.mvn(delegate)
    SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

    jdk(Defs.jdkVersion)

    // add build variables to environment so we can query for them
    preBuildSteps {
        environmentVariables {
            propertiesFile(PropertiesFiles.VERSIONS_PROPERTIES_FILE_PATH.value)
        }
    }

    goals("-B -e org.jacoco:jacoco-maven-plugin:${SflyDefaults.JACOCO_PLUGIN_VERSION}:prepare-agent install")

    publishers {
        SflyDefaults.updateVersionsBom(delegate)
        downstreamParameterized {
            trigger("${Defs.appName}-config") {
                parameters {
                    propertiesFile(PropertiesFiles.VERSIONS_PROPERTIES_FILE_PATH.value, true)
                    gitRevision()
                    currentBuild()
                }
            }
        }

        SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
    }

    SflyDefaults.slackPublisher(delegate, Defs.slackChannel)
}
