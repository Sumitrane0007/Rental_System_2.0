package com.indifarm.machineryrental.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps the URL path /proofs/**
        // to a physical folder on your computer located at:
        // [Your_Project_Directory]/proofs/
        exposeDirectory("proofs", registry);

        // This maps the URL path /uploads/**
        // to a physical folder on your computer located at:
        // [Your_Project_Directory]/uploads/
        exposeDirectory("uploads", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        // 1. Get the absolute physical path to the directory
        Path directory = Paths.get(System.getProperty("user.dir"), dirName);
        String physicalPath = directory.toFile().getAbsolutePath();

        // 2. Map the web URL to the physical path
        // (e.g., /proofs/** -> file:///C:/your-project/proofs/)
        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + physicalPath + "/");
    }
}