
# Jenkins Shared Library – Lab 22

A reusable Jenkins Shared Library developed as part of the **iVolve DevOps Training** to simplify CI/CD pipelines and reduce code duplication across projects.

## Overview

This library provides reusable Groovy functions for building applications, creating and scanning Docker images, publishing them to Docker Hub, and deploying applications to Kubernetes.

The functions accept configurable parameters, allowing the same library to be used across different projects.

## Repository Structure

```text
iVolve-jenkins-shared-library/
├── vars/
│   ├── buildApp.groovy
│   ├── buildImage.groovy
│   └── updateGitOpsManifest.groovy
└── README.md
```

## Shared Library Functions

| Function | Description |
|---|---|
| `buildApp()` | Checks out source code, installs dependencies or builds the application, and generates an image tag. |
| `buildImage()` | Builds a Docker image, scans it using Trivy, pushes it to Docker Hub, and removes the local image. |
| `updateGitOpsManifest()` | Updates the Kubernetes image tag, commits the manifest, and pushes it to GitHub for Argo CD synchronization. |

## Usage

Register the repository as a Global Pipeline Library in Jenkins, then import it into your Jenkinsfile:

```groovy
@Library('ivolve-shared-library@main') _
```

Call the functions with project-specific configurations:

```groovy
buildApp(
    appDir: 'app',
    buildCommand: 'npm ci'
)

buildImage(
    imageRepo: 'hager83/ivolve-app',
    dockerfile: 'Dockerfile',
    context: '.',
    registryCredentials: 'dockerhub-credentials',
    scanSeverity: 'HIGH,CRITICAL'
)

updateGitOpsManifest(
    manifest: 'kubernetes/deployment.yaml',
    container: 'ivolve-app',
    gitCredentials: 'github-private-repos'
)
```

## Technologies

Jenkins · Groovy · GitHub · Docker · Trivy · Docker Hub · Kubernetes · Argo CD · GitOps

## Related Project

**Application Repository:** `Hager-83/ivolve-app-cicd`

## Purpose

The goal of this lab is to separate reusable CI/CD logic from application-specific Jenkinsfiles, making pipelines easier to maintain, configure, and reuse across multiple projects.
