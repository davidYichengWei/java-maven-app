def gv

pipeline {
    agent any
    
    parameters {
        string(name: 'EC2_IP', defaultValue: '3.211.8.185', description: 'IP address of EC2 instance to deploy to')
    }

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
        stage('increment version') {
            steps {
                script {
                    gv.incrementVersion()
                }
            }
        }
        stage('build jar') {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }
        stage('build docker image') {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }
        stage('deploy') {
            environment {
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
                APP_NAME = 'java-maven-app'
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
            
        }
        stage('commit version update') {
            steps {
                script {
                    gv.commitVersion()
                }
            }
        }
    }
}