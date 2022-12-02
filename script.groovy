def buildApp() {
    echo "Building the application..."
}

def testApp() {
    if (${params.executeTests}) {
        echo "Testing the application..."
    }
}

def deployApp() {
    echo "Deploying the application..."
    echo "Deploying version ${params.VERSION}"
}

return this