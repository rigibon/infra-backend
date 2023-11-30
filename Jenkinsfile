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
          sh "docker tag backend-prueba rigibon/backend-prueba:${env.BUILD_NUMBER}"
          sh "docker push rigibon/backend-prueba:${env.BUILD_NUMBER}"
        }
      }

      stage('Deploy') {
          steps {
            sh "ssh rigibon@192.168.0.211 'kubectl delete deployment backend-deployment'"
            sh "ssh rigibon@192.168.0.211 'kubectl delete service backend-deployment'"
            sh "ssh rigibon@192.168.0.211 'kubectl create deployment backend-deployment --image=rigibon/backend-prueba:${env.BUILD_NUMBER}'"
            sh 'ssh rigibon@192.168.0.211 "kubectl expose deployment backend-deployment --port=4200 --target-port=4200 --type=LoadBalancer"'
          }
      }
  }
}
