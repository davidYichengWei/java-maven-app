
library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
    [
        $class: 'GitSCMSource',
        remote: 'https://github.com/davidYichengWei/jenkins-shared-library.git',
        credentialsId: 'git-credential'
    ]
)

def gv

pipeline {
    agent any

    tools {
        maven "maven-3.8"
    }

    stages {
        stage('init') {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage('test') {
            steps {
                script {
                    gv.testApp()
                }
            }
        }
        stage('build jar') {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage('build and push docker image') {
            steps {
                script {
                    buildImage('yichengwei/demo-jenkins:jma-3.0')
                    dockerLogin()
                    dockerPush('yichengwei/demo-jenkins:jma-3.0')
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }
}