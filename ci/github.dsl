import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.DefaultJobOptions
import policies.sfly.SflyDefaults

((DslFactory) this).mavenJob("${Defs.appName}-pullrequest-compile") {
    SflyDefaults.gitDescription(delegate, "Compile and test ${Defs.appName} service from pull request.", Defs.repoUrl, this)

    // use standard Shutterfly project and maven configurations
    SflyDefaults.basic(delegate, [DefaultJobOptions.SLAVE,
                                  DefaultJobOptions.LOG_ROTATION,
                                  DefaultJobOptions.PERMISSIONS,
                                  DefaultJobOptions.WRAPPERS])

    SflyDefaults.mvn(delegate, true, false, 'pom.xml')
    SflyDefaults.addRunPermissions(delegate, Defs.devTeam)
    SflyDefaults.secretBindingConfig(delegate, 'thycotic_api_username', 'thycotic_api_password', 'thycotic_api_cred')

    jdk(Defs.jdkVersion)

    scm {
        SflyDefaults.git(delegate, Defs.repoUrl)
    }

    SflyDefaults.ghePullRequestTrigger(delegate)

    SflyDefaults.buildSpringboardEnvConfig(delegate, Defs.localITTestEnv)

    // run IT + unit tests
    SflyDefaults.runLocalSpringboardITs(delegate, false, true)
}
