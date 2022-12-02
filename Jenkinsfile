pipeline {
    agent any

    tools {
        maven "maven-3.8"
    }

    stages {
        stage('build jar') {
            steps {
                script {
                    echo "Building the application..."
                    sh "mvn package"
                }
            }
        }
        stage('build docker image') {
            steps {
                script {
                    echo "Building the docker image..."

                    withCredentials([usernamePassword(credentialsId: 'DockerHub-credential', 
                        usernameVariable: 'USER', passwordVariable: 'PWD')]) 
                    {
                        sh "docker build -t yichengwei/demo-jenkins:jma-2.0 ."
                        sh "echo $PWD | docker login -u ${USER} --password-stdin"
                        sh "docker push yichengwei/demo-jenkins:jma-2.0"
                    }
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                    echo "Deploying the application..."
                }
            }
        }
    }
}
