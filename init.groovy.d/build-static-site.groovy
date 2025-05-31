import jenkins.model.*
import hudson.model.*
import hudson.plugins.git.*
import hudson.tasks.Shell

def jenkins = Jenkins.getInstanceOrNull()
if (jenkins != null) {
    def jobName = "build-static-site"

    if (jenkins.getItem(jobName) == null) {
        def job = jenkins.createProject(FreeStyleProject, jobName)
        job.setDescription("Builds and runs the static Docker site.")

        def gitSCM = new GitSCM(
            [new UserRemoteConfig("https://github.com/Erendmrkndmr/Erendmrkndmr.github.io", null, null, null)],
            [new BranchSpec("*/dev")],
            false, Collections.emptyList(), null, null, Collections.emptyList()
        )
        job.setScm(gitSCM)

        def shell = new Shell('''
            docker stop static-site || true
            docker rm static-site || true
            docker rmi static-site || true
            docker build -t static-site .
            docker run -d --name static-site -p 80:80 static-site
        ''')

        job.getBuildersList().add(shell)
        job.save()
    }
}
