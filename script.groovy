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
    env.IMAGE_NAME = "${DOCKER_SERVER}/${DOCKER_REPO}:${version}-${BUILD_NUMBER}" // commom practice to append jenkins build number to version
}

def buildJar() {
    echo "Building the application..."
    sh "mvn clean package" // clean existing jar files before building
}

def buildImage() {
    echo "Building the docker image and pushing to ECR..."

    withCredentials([usernamePassword(credentialsId: 'ecr-credential', 
        usernameVariable: 'USER', passwordVariable: 'PWD')]) 
    {
        sh "docker build -t ${IMAGE_NAME} ."
        sh "echo $PWD | docker login -u ${USER} --password-stdin ${DOCKER_SERVER}"
        sh "docker push ${IMAGE_NAME}"
    }
}

def provisionEKS() {
    echo "Provisioning EKS cluster..."

    dir('terraform') {
        sh "terraform init"
        sh "terraform apply -auto-approve"
        // Set the name of the EKS cluster as an environment variable
        env.EKS_CLUSTER_NAME = sh(
            script: "terraform output eks_cluster_name", 
            returnStdout: true
        ).trim()
    }
}

def deployApp() {
    echo "Configuring kubectl..."
    sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}"

    // Create a secret for the ECR registry
    env.ECR_PWD = sh(
        script: "aws ecr get-login-password --region us-east-1", 
        returnStdout: true
    ).trim()

    sh "kubectl create secret docker-registry ecr-registry-key \
        --docker-server=${DOCKER_SERVER} \
        --docker-username=AWS \
        --docker-password=${ECR_PWD}"

    echo "Deploying to EKS cluster..."

    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -' // envsubst: replace environment variables in a file
    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f -'
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
        sh 'git push origin HEAD:k8s-ci/cd-ecr'
    }
}

return this