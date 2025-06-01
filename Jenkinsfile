pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
               sh './gradlew clean test'
            }
        }
    }

    post {
        always {
            // This publishes test results to Jenkins UI
              junit '**/build/test-results/test/*.xml'
        }
    }
}
