package com.example.javaqualifier.config;

import com.example.javaqualifier.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private final WebhookService webhookService;

    public AppStartupRunner(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @Override
    public void run(String... args) {
        webhookService.startProcess();
    }
}
