import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.DefaultJobOptions
import policies.sfly.SflyDefaults

((DslFactory) this).mavenJob("${Defs.appName}-sonar-IT") {

    SflyDefaults.gitDescription(
        delegate,
        "Analyze ${Defs.appName} project with sonar.",
        Defs.repoUrl,
        this)

    SflyDefaults.basic(delegate, [DefaultJobOptions.SLAVE,
                                  DefaultJobOptions.LOG_ROTATION,
                                  DefaultJobOptions.JDK,
                                  DefaultJobOptions.PERMISSIONS,
                                  DefaultJobOptions.WRAPPERS])

    SflyDefaults.addRunPermissions(delegate, Defs.devTeam)

    jdk(Defs.jdkVersion)

    triggers {
        cron('@daily')
    }

    parameters {
        stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
    }

    scm {
        SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
    }

    SflyDefaults.buildSpringboardEnvConfig(delegate, Defs.localITTestEnv)

    // run ITs only, publish to Sonar
    SflyDefaults.runLocalSpringboardITs(delegate, true)
}
