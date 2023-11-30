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
      }
}