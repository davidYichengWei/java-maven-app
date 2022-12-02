
pipeline {

    agent any

    parameters {
        choice(name: 'VERSION', choices: ['1.1', '1.2', '1.3'], description: '')
        booleanParam(name: 'executeTests', defaultValue: true, description: 'Execute tests')
    }


    stages {

        stage("build") {
            steps {
                echo "Building the application..."
            }
        }

        stage("test") {
            when {
                expression {
                    params.executeTests == true
                }
            }

            steps {
                echo "Testing the application..."
            }
        }

        stage("deploy") {
            steps {
                echo "Deploying the application..."
                echo "Deploying version ${params.VERSION}"

            }
        }

    }
}