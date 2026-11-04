
def call(Map config = [:]) {
    String appDir = config.get('appDir', '.')
    String buildCommand = config.get('buildCommand', 'npm ci')

    checkout scm

    dir(appDir) {
        sh buildCommand
    }

    env.IMAGE_TAG = "${env.BUILD_NUMBER}-${env.GIT_COMMIT.take(7)}"

    echo "Application build completed."
    echo "Image tag: ${env.IMAGE_TAG}"
}
