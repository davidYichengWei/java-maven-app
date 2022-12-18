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
            steps {
                script {
                    gv.deployApp(params.EC2_IP)
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