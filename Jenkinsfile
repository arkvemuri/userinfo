pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'Java21'
    }

    environment {
        GITHUB_REPO_URL = 'https://github.com/arkvemuri/userinfo.git'
        BRANCH_NAME = 'master'
        APP_VERSION = '1.0.0'
        SONAR_SERVER = 'SonarQube'
        // Define SonarQube authentication token
        SONAR_TOKEN = credentials('SONAR_TOKEN')
    }

    stages {
        stage('Checkout') {
            steps {
                // Clean workspace before checking out code
                cleanWs()
                git branch: "${env.BRANCH_NAME}",
                    url: "${env.GITHUB_REPO_URL}",
                    changelog: true,
                    poll: true
            }
        }

        stage('Build') {
            steps {
                // Use bat for Windows
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
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
                    bat """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=userinfo \
                        -Dsonar.projectName=userinfo \
                        -Dsonar.projectVersion=${APP_VERSION} \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.junit.reportPaths=target/surefire-reports \
                        -Dsonar.java.coveragePlugin=jacoco \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.login=${SONAR_TOKEN}
                    """
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
        success {
            echo "Successfully built and analyzed version ${env.APP_VERSION}"
        }
        failure {
            echo 'Build or analysis failed!'
        }
        always {
            // Clean workspace after build
            cleanWs()
        }
    }
}
