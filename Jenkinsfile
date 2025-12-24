pipeline {
    agent any

    tools {
        maven 'maven-3.9.12'   // must match Jenkins Global Tool name
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/TanmayQA/SeleniumEndtoEndProject.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
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

                    <p>The latest Jenkins build has completed successfully.</p>

                    <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                    <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                    <p><b>Build Status:</b>
                        <span style="color:green;"><b>SUCCESS</b></span>
                    </p>
                    <p><b>Build URL:</b>
                        <a href="${env.BUILD_URL}">${env.BUILD_URL}</a>
                    </p>

                    <p><b>Extent Report:</b>
                        <a href="${env.BUILD_URL}HTML_20Extent_20Spark_20Report/">
                            Click here
                        </a>
                    </p>

                    <br/>
                    <p>Best regards,<br/>
                    Automation Team</p>
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

                    <p>The latest Jenkins build has
                        <b style="color:red;">FAILED</b>.
                    </p>

                    <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                    <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                    <p><b>Build Status:</b>
                        <span style="color:red;"><b>FAILED</b></span>
                    </p>
                    <p><b>Build URL:</b>
                        <a href="${env.BUILD_URL}">${env.BUILD_URL}</a>
                    </p>

                    <p><b>Extent Report (if generated):</b>
                        <a href="${env.BUILD_URL}HTML_20Extent_20Spark_20Report/">
                            Click here
                        </a>
                    </p>

                    <br/>
                    <p>Please review the logs and report.</p>

                    <p>Regards,<br/>
                    Automation Team</p>
                </body>
                </html>
                """,
                attachLog: true
            )
        }
    }
}
