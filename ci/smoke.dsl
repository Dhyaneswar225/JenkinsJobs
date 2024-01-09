import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults

for (env in Defs.envs) {
    ((DslFactory) this).freeStyleJob("${Defs.ecsName}-smoke-${env}") {
        SflyDefaults.gitDescription(
            delegate,
            "Smoke test ECS ${env} deploy of ${Defs.appName}.",
            Defs.repoUrl,
            this)

        SflyDefaults.addRunPermissions(delegate, Defs.devTeam)
        SflyDefaults.basic(delegate)
        configure { node -> node.remove(node / triggers) }

        parameters {
            stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
        }

        scm {
            SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
        }

        publishers {
            SflyDefaults.emailTriggers(delegate, Defs.alertEmails)
        }
    }
}
