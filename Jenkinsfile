pipeline {
  agent any
  environment {
    DOCKERHUB_CREDENTIALS = credentials('dockerhub')
  }
  stages {
      stage('SonarQube') {
            steps {
              withSonarQubeEnv(installationName: 'sonarqube') {
                sh 'mvn package org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
              }
            }
          }

      stage("Quality gate") { 
        steps { 
          waitForQualityGate abortPipeline: true 
        } 
      }

      stage("Maven test") {
        steps {
          sh 'mvn test'
        }
      }

      stage('Push') {
        steps {
          sh 'docker login -u ${DOCKERHUB_CREDENTIALS_USR} -p ${DOCKERHUB_CREDENTIALS_PSW}'
          sh 'docker build . -t backend-prueba'
          sh "docker tag backend-prueba ${DOCKERHUB_CREDENTIALS_USR}/backend-prueba:${env.BUILD_NUMBER}"
          sh "docker push ${DOCKERHUB_CREDENTIALS_USR}/backend-prueba:${env.BUILD_NUMBER}"
        }
      }

      stage('Deploy') {
          steps {
            sh "ssh rigibon@192.168.0.211 'kubectl set image deployment/backend-deployment backend-prueba=${DOCKERHUB_CREDENTIALS_USR}/backend-prueba:${env.BUILD_NUMBER}'"
          }
      }
  }
}
