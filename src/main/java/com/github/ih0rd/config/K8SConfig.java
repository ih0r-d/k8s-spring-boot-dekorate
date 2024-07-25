package com.github.ih0rd.config;

import io.dekorate.docker.annotation.DockerBuild;
import io.dekorate.kubernetes.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@KubernetesApplication(
//        name = "#{@environment.getProperty('spring.application.name')}",
        name = "k8s-spring-boot-dekorate",
        labels = @Label(
                key = "app-version",
                value = "0.0.1-SNAPSHOT"
        ),
        ports = @Port(
                name = "http",
                containerPort = 8080
        ),
        livenessProbe = @Probe(
                httpActionPath = "/health/liveness",
                initialDelaySeconds = 5,
                timeoutSeconds = 3,
                failureThreshold = 10
        ),
        readinessProbe = @Probe(
                httpActionPath = "/health/readiness",
                initialDelaySeconds = 5,
                timeoutSeconds = 3,
                failureThreshold = 10
        ),
        requestResources = @ResourceRequirements(
                memory = "64Mi",
                cpu = "1m"
        ),
        limitResources = @ResourceRequirements(
                memory = "256Mi",
                cpu = "5m"
        )
)
@DockerBuild()
public class K8SConfig {
}
