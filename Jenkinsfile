pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'sudo apt update'
                sh 'sudo apt install maven'
                sh 'mvn clean package'
            }
        }
    }
}
