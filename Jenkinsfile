def gv

pipeline {
    agent any
    
    parameters {
        string(name: 'Environment type', defaultValue: 'test', description: 'Type of environment')
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
        stage('provision server') {
            // Environment variables for AWS authentication
            environment {
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
                TF_VAR_environment = params.Environment_type
            }
            steps {
                script {
                    gv.provisionServer()
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
        stage('commit version update') {
            steps {
                script {
                    gv.commitVersion()
                }
            }
        }
    }
}