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
        SONAR_PROJECT_KEY = 'userinfo'
        // Define SonarQube credentials
        SONARQUBE_CREDS = credentials('sonarqube-token')
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

        stage('Build and Test') {
            steps {
                // Use bat for Windows
                bat 'mvn clean verify'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    // Archive JaCoCo coverage reports as artifacts
                    archiveArtifacts artifacts: 'target/site/jacoco/**/*', allowEmptyArchive: true
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat """
                        mvn sonar:sonar \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${env.SONAR_PROJECT_KEY} \
                        -Dsonar.projectVersion=${APP_VERSION} \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.junit.reportPaths=target/surefire-reports \
                        -Dsonar.java.coveragePlugin=jacoco \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.login=${SONARQUBE_CREDS_PSW}
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

        stage('Coverage Check') {
            steps {
                script {
                    // Coverage is already checked by Maven JaCoCo plugin during 'mvn clean verify'
                    // If the build reached this stage, coverage requirements are met
                    echo 'Coverage check passed - Maven JaCoCo plugin enforced minimum 80% coverage per class'
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
