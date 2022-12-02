def testApp() {
    echo "Executing pipeline for branch ${BRANCH_NAME}"
    echo "Testing the application..."
}

def buildJar() {
    if (BRANCH_NAME == "jenkins-jobs") {
        echo "Building the application..."
        sh "mvn package"
    }
}

def buildImage() {
    if (BRANCH_NAME == "jenkins-jobs") {
        echo "Building the docker image..."

        withCredentials([usernamePassword(credentialsId: 'DockerHub-credential', 
            usernameVariable: 'USER', passwordVariable: 'PWD')]) 
        {
            sh "docker build -t yichengwei/demo-jenkins:jma-2.0 ."
            sh 'echo $PWD | docker login -u $USER --password-stdin'
            sh "docker push yichengwei/demo-jenkins:jma-2.0"
        }
    }
}

def deployApp() {
    if (BRANCH_NAME == "jenkins-jobs") {
        echo "Deploying the application..."
    }
}

return this