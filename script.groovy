def testApp() {
    echo "Testing the application..."
    sh "mvn test"
}

def deployApp() {
    echo "Deploying the application..."
}

return this