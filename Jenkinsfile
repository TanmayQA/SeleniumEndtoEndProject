pipeline {
    agent any

    tools {
        maven 'maven_lib'
    }

    environment {
        COMPOSE_PATH   = "${WORKSPACE}/docker"
        SELENIUM_GRID  = "true"
        SELENIUM_IMAGE = "selenium/standalone-chromium:4.34.0"
    }

    stages {

        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Docker Environment') {
            steps {
                echo "Preparing Docker environment..."
                sh '''
                    set -e

                    echo "Cleaning dangling / partial Docker images..."
                    docker image prune -f || true

                    echo "Pulling Selenium image (Jenkins controlled)..."
                    for i in 1 2 3; do
                      docker pull ${SELENIUM_IMAGE} && break
                      echo "Retry $i failed. Retrying in 10 seconds..."
                      sleep 10
                    done
                '''
            }
        }

        stage('Start Selenium Grid') {
            steps {
                echo "Starting Selenium via Docker Compose..."
                sh """
                    docker compose -f ${COMPOSE_PATH}/docker-compose.yml up -d
                """
                echo "Waiting for Selenium to be ready..."
                sleep 30
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -DseleniumGrid=true'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -DseleniumGrid=true'
            }
        }

        stage('Reports') {
            steps {
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/ExtentReport',
                    reportFiles: 'index.html',
                    reportName: 'Extent Spark Report'
                ])
            }
        }
    }

    post {

        always {
            echo "Stopping Selenium..."
            sh """
                docker compose -f ${COMPOSE_PATH}/docker-compose.yml down || true
            """

            archiveArtifacts artifacts: 'target/ExtentReport/*.html', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }

        success {
            emailext(
                to: 'tanmay.sharma9411@gmail.com',
                subject: "Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                body: """
                <html>
                <body>
                    <p>Hello Team,</p>
                    <p>The Jenkins build completed
                        <b style="color:green;">SUCCESSFULLY</b>.
                    </p>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build:</b> #${env.BUILD_NUMBER}</p>
                    <p><a href="${env.BUILD_URL}">View Build</a></p>
                    <p>
                        <a href="${env.BUILD_URL}HTML_20Extent_20Spark_20Report/">
                            View Extent Report
                        </a>
                    </p>
                </body>
                </html>
                """,
                attachLog: true
            )
        }

        failure {
            emailext(
                to: 'tanmay.sharma9411@gmail.com',
                subject: "Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                body: """
                <html>
                <body>
                    <p>Hello Team,</p>
                    <p>The Jenkins build has
                        <b style="color:red;">FAILED</b>.
                    </p>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build:</b> #${env.BUILD_NUMBER}</p>
                    <p><a href="${env.BUILD_URL}">View Build</a></p>
                </body>
                </html>
                """,
                attachLog: true
            )
        }
    }
}
