import jenkins.model.*
import hudson.model.*

def jenkins = Jenkins.getInstance()

def jobName = "build-static-site"
if (jenkins.getItem(jobName) == null) {
    def job = new FreeStyleProject(jenkins, jobName)
    job.setDescription("Clones static site repo and builds Docker container")

    def gitScm = new hudson.plugins.git.GitSCM("https://github.com/Erendmrkndmr/Erendmrkndmr.github.io")
    job.setScm(gitScm)

    def shellStep = new hudson.tasks.Shell('''
        docker stop static-site || true
        docker rm static-site || true
        docker rmi static-site || true

        docker build -t static-site .
        docker run -d --name static-site -p 80:80 static-site
    ''')

    job.getBuildersList().add(shellStep)
    jenkins.reload()
    jenkins.add(job, jobName)
    job.save()
}
