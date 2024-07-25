package com.github.ih0rd.config;

import io.dekorate.docker.config.DockerBuildConfig;
import io.dekorate.docker.config.DockerBuildConfigBuilder;
import io.dekorate.kubernetes.config.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DekorateConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version}")
    private String appVersion;

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public DockerBuildConfig dockerBuildConfig() {
        return new DockerBuildConfigBuilder()
                .withName(appName)
                .withVersion(appVersion)
                .build();
    }

    @Bean
    public KubernetesConfig kubernetesConfig() {
        var port = Port.newBuilder().withName("http").withContainerPort(serverPort).build();
        var label = new Label("appVersion", appVersion, new String[]{});
        var livenessProbe = new Probe("/health/liveness", "", "", "", 5, 10, 3, 200, 500);
        var readinessProbe = new Probe("/health/readiness", "", "", "", 5, 10, 3, 200, 500);
        var resourceRequirements = ResourceRequirements.newBuilder().withCpu("1m").withMemory("128Mi").build();
        var limitResources = ResourceRequirements.newBuilder().withCpu("5m").withMemory("512Mi").build();
        return new KubernetesConfigBuilder()
                .withName(appName)
                .withLabels(label)
                .withPorts(port)
                .withLivenessProbe(livenessProbe)
                .withReadinessProbe(readinessProbe)
                .withReplicas(3)
                .withRequestResources(resourceRequirements)
                .withLimitResources(limitResources)
                .build();
    }

}
