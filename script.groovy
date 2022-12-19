def testApp() {
    echo "Testing the application...."
    sh "mvn test"
}

def incrementVersion() {
    echo "Incrementing app version"
    sh 'mvn build-helper:parse-version versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        versions:commit'

    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1] // [0]: <version>(.+)</version>, [0][1]: (.+)
    env.IMAGE_NAME = "yichengwei/demo-jenkins:${version}-${BUILD_NUMBER}" // commom practice to append jenkins build number to version
}

def buildJar() {
    echo "Building the application..."
    sh "mvn clean package" // clean existing jar files before building
}

def buildImage() {
    echo "Building the docker image..."

    withCredentials([usernamePassword(credentialsId: 'DockerHub-credential', 
        usernameVariable: 'USER', passwordVariable: 'PWD')]) 
    {
        sh "docker build -t ${IMAGE_NAME} ."
        sh "echo $PWD | docker login -u ${USER} --password-stdin"
        sh "docker push ${IMAGE_NAME}"
    }
}

def deployApp(EC2_IP) {
    echo "Deploying the application...."
    def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME}"
    def ec2Instance = "ec2-user@${EC2_IP}"

    sshagent(['ec2-server-key']) {
        sh "scp docker-compose.yml ${ec2Instance}:/home/ec2-user"
        sh "scp server-cmds.sh ${ec2Instance}:/home/ec2-user"
        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
    }
}

def commitVersion() {
    withCredentials([usernamePassword(credentialsId: 'git-credential', 
        usernameVariable: 'USER', passwordVariable: 'PWD'), string(credentialsId: 'github-token', variable: 'TOKEN')]) 
    {
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'

        sh 'git status'
        sh 'git branch'
        sh 'git config --list'

        sh "git remote set-url origin https://${USER}:${TOKEN}@github.com/davidYichengWei/java-maven-app.git"
        sh 'git add .'
        sh 'git commit -m "CI: Version bump"'
        sh 'git push origin HEAD:jenkins-jobs'
    }
}

return this