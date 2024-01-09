import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults

import static Defs

((DslFactory) this).freeStyleJob("${Defs.appName}-dockerize-service") {

    SflyDefaults.gitDescription(
        delegate,
        "Build and archive ${Defs.appName} docker image.",
        Defs.repoUrl,
        this)

    SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

    parameters {
        stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
    }

    scm {
        SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
    }

    blockOnDownstreamProjects()

    SflyDefaults.simpleDockerBuild(delegate, null, 'docker', "VERSION=\${dependency_version} clean service deploy")
    configure { node -> node.remove(node / triggers) }

    publishers {
        SflyDefaults.emailTriggers(delegate, Defs.devTeam)
    }

    SflyDefaults.triggerJob(delegate, "${Defs.appName}-dockerize-config", {
        currentBuild()
        gitRevision()
    })
}
