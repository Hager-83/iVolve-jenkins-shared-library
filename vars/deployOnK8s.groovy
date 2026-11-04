
def call(Map config = [:]) {
    String namespace = config.get('namespace', 'default')
    String deployment = config.deployment
    String container = config.container
    String manifest = config.get('manifest', 'kubernetes/deployment.yaml')
    String credentialsId = config.get('kubeCredentials', 'kubeconfig-ivolve')
    String kubectl = config.get('kubectl', 'kubectl')

    if (!deployment?.trim() || !container?.trim()) {
        error('deployment and container are required')
    }

    if (!env.FULL_IMAGE?.trim()) {
        error('FULL_IMAGE is required. Run buildImage first.')
    }

    withEnv([
        "DEPLOY_NAMESPACE=${namespace}",
        "DEPLOY_NAME=${deployment}",
        "DEPLOY_CONTAINER=${container}",
        "DEPLOY_MANIFEST=${manifest}",
        "KUBECTL_BIN=${kubectl}"
    ]) {
        withCredentials([
            file(
                credentialsId: credentialsId,
                variable: 'KUBECONFIG'
            )
        ]) {
            sh '''
                set -e

                echo "Updating deployment manifest..."

                "$KUBECTL_BIN" set image \
                  -f "$DEPLOY_MANIFEST" \
                  "$DEPLOY_CONTAINER=$FULL_IMAGE" \
                  --local \
                  -o yaml > deployment-updated.yaml

                echo "Deploying updated manifest..."

                "$KUBECTL_BIN" apply \
                  -f deployment-updated.yaml \
                  -n "$DEPLOY_NAMESPACE"

                echo "Waiting for deployment rollout..."

                "$KUBECTL_BIN" rollout status \
                  deployment/"$DEPLOY_NAME" \
                  -n "$DEPLOY_NAMESPACE" \
                  --timeout=300s
            '''
        }
    }

    echo "Deployment completed: ${deployment}"
}
