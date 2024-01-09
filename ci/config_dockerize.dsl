import javaposse.jobdsl.dsl.DslFactory
import policies.Slaves
import policies.sfly.SflyDefaults

import static Defs

((DslFactory) this).matrixJob("${Defs.appName}-dockerize-config") {
    SflyDefaults.gitDescription(delegate, "Build and archive ${Defs.appName} config docker images.", Defs.repoUrl, this)

    SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

    parameters {
        stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
    }

    scm {
        SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
    }

    SflyDefaults.secretBindingConfig(delegate, 'thycotic_api_username', 'thycotic_api_password', 'thycotic_api_cred')

    blockOnDownstreamProjects()

    SflyDefaults.setupAxes(delegate, Slaves.DOCKER_BUILD.value, Defs.envs)

    SflyDefaults.simpleDockerBuild(delegate, null, 'docker', "VERSION=\${dependency_version} ENV=\${Environment} clean config deploy")
    configure { node -> node.remove(node / triggers) }

    publishers{
        SflyDefaults.emailTriggers(delegate, Defs.devTeam)
    }

    SflyDefaults.triggerJob(delegate, "${Defs.appName}-localIT-${Defs.initialDeploymentEnv}", {
        gitRevision()
        currentBuild()
        predefinedProps([ "dockerTag": "\${scm_revision}"])
    })
}
