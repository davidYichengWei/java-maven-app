def gv

pipeline {

    agent any

    parameters {
        choice(name: 'VERSION', choices: ['1.1', '1.2', '1.3'], description: '')
        booleanParam(name: 'executeTests', defaultValue: true, description: 'Execute tests')
    }


    stages {

        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage("build") {
            steps {
                script {
                    gv.buildApp()
                }
            }
        }

        stage("test") {
            steps {
                script {
                    gv.testApp()
                }
            }
        }

        stage("deploy") {
            steps {
                script {
                    env.ENV = input message: "Select the environment to deploy to", ok: "Select", 
                        parameters: [choice(name: 'ENV_ONE', choices: ['dev', 'staging', 'prod'], description: '')]

                    gv.deployApp()
                    echo "Deploying to ${ENV}"
                }
            }
        }

    }
}