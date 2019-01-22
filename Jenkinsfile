pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'apt-get update'
                sh 'apt-get install maven'
                sh 'mvn clean package'
            }
        }
    }
}
