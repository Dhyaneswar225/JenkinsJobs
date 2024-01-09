import Defs
import javaposse.jobdsl.dsl.DslFactory
import policies.sfly.SflyDefaults

for (env in Defs.envs) {
    ((DslFactory) this).buildPipelineView("${Defs.appName}-${env}-build-pipeline") {
        SflyDefaults.buildPipelineViewDefaults(delegate)
        filterBuildQueue()
        filterExecutors()
        title("${env.capitalize()} build pipeline for ${Defs.appName}")
        SflyDefaults.gitDescription(
            delegate,
            "Build pipeline for ${Defs.appName} ${env.capitalize()}.",
            Defs.repoUrl,
            this)
        displayedBuilds(10)
        consoleOutputLinkStyle(OutputStyle.NewWindow)
        if (env == Defs.initialDeploymentEnv) {
            selectedJob("${Defs.appName}-compile")
        } else if (env == 'prod') {
            selectedJob("${Defs.appName}-deploy-${env}")
        } else {
            selectedJob("${Defs.appName}-service-${env}-pipeline-init")
        }
        alwaysAllowManualTrigger()
        showPipelineParameters()
        refreshFrequency(60)
    }
}
