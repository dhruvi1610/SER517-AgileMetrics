package edu.asu.cassess.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("edu.asu.cassess.service")
public class ServiceConfig {
    private static String AG_URL = "http://localhost:3000";

    public static String getAG_URL() {
        return AG_URL;
    }
}
