import javaposse.jobdsl.dsl.DslFactory
import policies.DefaultJobOptions
import policies.sfly.SflyDefaults

import static Defs

for (env in SflyDefaults.getEnvs()) {
    ((DslFactory) this).mavenJob("${Defs.appName}-localIT-${env}") {
        SflyDefaults.gitDescription(
            delegate,
            "Run local IT tests for $env prior to deployment; automatically deploy on sucess except for production",
            Defs.repoUrl,
            this)

        SflyDefaults.basic(delegate, [DefaultJobOptions.SLAVE,
                                      DefaultJobOptions.LOG_ROTATION,
                                      DefaultJobOptions.PERMISSIONS,
                                      DefaultJobOptions.WRAPPERS])

        // delegate is the job, true is to prepare (default), false is to not deploy to artifactory (true is default)
        SflyDefaults.mvn(delegate, false, false, 'pom.xml')

        parameters {
            stringParam('Branch', "${Defs.branch}", "Git branch name or commit hash, defaults to ${Defs.branch}")
        }

        scm {
            SflyDefaults.git(delegate, Defs.repoUrl, "\${Branch}")
        }

        jdk(Defs.jdkVersion)

        SflyDefaults.buildSpringboardEnvConfig(delegate, env)

        // run IT tests only
        SflyDefaults.runLocalSpringboardITs(delegate)

        if (env != 'prod') {
            SflyDefaults.triggerJob(delegate, "${Defs.ecsName}-deploy-${env}", {
                gitRevision()
                currentBuild()
            })
        }
    }
}

