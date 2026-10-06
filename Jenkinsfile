// Pipeline del CI local. Ejecuta la suite en contenedores (docker-compose.yml) y publica el reporte Allure.
// Si algún escenario falla, el pipeline termina en estado fallido; el reporte se publica siempre.
pipeline {
    agent any

    parameters {
        choice(name: 'TEST_TYPE', choices: ['component', 'e2e', 'all'],
               description: 'Tipo de pruebas: component (@component), e2e (@e2e) o all (ambas)')
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        // Nombre fijo del proyecto de Compose para que siempre se limpien los mismos contenedores
        COMPOSE_PROJECT_NAME = 'pom-ci'
    }

    stages {
        stage('Tests') {
            steps {
                sh '''
                    case "$TEST_TYPE" in
                        component) export TEST_TASK=componentTest ;;
                        e2e)       export TEST_TASK=e2eTest ;;
                        all)       export TEST_TASK=functionalTest ;;
                    esac
                    export HOST_UID=$(id -u) HOST_GID=$(id -g)
                    docker compose up --build --abort-on-container-exit --exit-code-from runner
                '''
            }
        }
    }

    post {
        always {
            // Los contenedores se detienen aunque las pruebas fallen
            sh 'docker compose down || true'
            publishHTML(target: [
                reportName: 'Allure Report',
                reportDir: 'build/reports/allure-report/allureReport',
                reportFiles: 'index.html',
                keepAll: true,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])
            archiveArtifacts artifacts: 'build/allure-results/**', allowEmptyArchive: true
        }
    }
}
