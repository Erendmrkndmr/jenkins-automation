import jenkins.model.*
import hudson.model.*
import hudson.tasks.*

def jenkins = Jenkins.getInstanceOrNull()

if (jenkins != null) {
    def jobName = "build-static-site"

    if (jenkins.getItem(jobName) == null) {
        def job = jenkins.createProject(FreeStyleProject, jobName)
        job.setDescription("Builds and runs the static Docker site.")

        def shell = new Shell('''
            git clone https://github.com/Erendmrkndmr/Erendmrkndmr.github.io || true
            cd Erendmrkndmr.github.io
            docker stop static-site || true
            docker rm static-site || true
            docker rmi static-site || true
            docker build -t static-site .
            docker run -d --name static-site -p 80:80 -v /var/run/docker.sock:/var/run/docker.sock static-site
        ''')

        job.getBuildersList().add(shell)
        job.save()
    }
}
