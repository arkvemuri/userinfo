pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        GITHUB_REPO_URL = 'https://github.com/arkvemuri/userinfo.git'
        SONAR_SERVER = 'SonarQube'
    }

    stages {
        stage('Checkout') {
            steps {
                // Clean workspace before checking out code
                cleanWs()
                git branch: 'main',
                    url: "${env.GITHUB_REPO_URL}"
            }
        }

        stage('Build') {
            steps {
                // Build the application and run unit tests
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                // Run tests and generate test reports
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(env.SONAR_SERVER) {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=userinfo \
                        -Dsonar.projectName=userinfo \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.junit.reportPaths=target/surefire-reports \
                        -Dsonar.java.coveragePlugin=jacoco \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                // Archive the built artifacts
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            // Clean workspace after build
            cleanWs()
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
