pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'apt update'
                sh 'apt install maven'
                sh 'mvn clean package'
            }
        }
    }
}
