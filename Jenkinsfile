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
               bat 'gradlew.bat test'
            }
        }
    }

    post {
        always {
             junit '**/build/test-results/test/*.xml'
              archiveArtifacts artifacts: 'build/reports/tests/test/index.html', fingerprint: true
        }
    }
}
