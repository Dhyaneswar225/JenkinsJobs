import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.DockerImages
import policies.sfly.SflyDefaults

((DslFactory) this).freeStyleJob("${Defs.appName}-checkmarx") {
    SflyDefaults.gitDescription(delegate, "Checkmarx scan ${Defs.appName}.", Defs.repoUrl, this)

    SflyDefaults.basic(delegate)

    SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

    SflyDefaults.dockerImage(delegate, DockerImages.JENKINS_SLAVE_CENTOS7_MAVEN)

    configure { node -> node.remove(node / triggers) }

    // delete the below line to enable CheckMarx scanning
    disabled()

    triggers { cron('@weekly') }

    wrappers { preBuildCleanup() }

    parameters {
        stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
    }

    scm {
        SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
    }

    SflyDefaults.asyncCheckmarxScan(delegate, Defs.appName + "-cx")

    publishers {
        SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
    }
}
