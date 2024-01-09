import policies.JavaVersions

class Defs {
    public static final def jdkVersion = JavaVersions.CORRETTO_11.value
    public static final def repoName = 'shutterfly/test-service-example'
    public static final def repoUrl = "git@github.com:sflyinc-shutterfly:${repoName}.git"
    public static final def appName = 'test-service-example'
    public static final def branch = 'master'
    public static final def artifactId = 'test-service-example'
    public static final def localITTestEnv = 'dev'
    public static final def initialDeploymentEnv = 'dev'
    public static final def slackChannel = "#test-service-example"
    public static final def thycoticAccount = 'db-thycotic'

    // Teams
    public static final def devTeam = [] // single-quoted list of dev usernames
    public static final def alertEmails = [] // single-quoted email address

    // Amazon ECS
    public static final def ecsName = "${appName}-ecs"
    public static final def awsAccountPrefix = "sfly-aws-legacy"
    public static final int canaryCount = 1

    // Environments
    public static final def envs = ["$initialDeploymentEnv", "beta", "stage", "prod"]
}
