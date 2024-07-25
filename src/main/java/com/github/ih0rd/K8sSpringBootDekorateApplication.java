package com.github.ih0rd;

import io.dekorate.docker.annotation.DockerBuild;
import io.dekorate.kubernetes.annotation.KubernetesApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@KubernetesApplication
@DockerBuild
public class K8sSpringBootDekorateApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sSpringBootDekorateApplication.class, args);
    }

}
