package com.qzmagic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SwaggerUrlPrinter implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${springdoc.api-docs.path:/v3/api-docs}")
    private String apiDocsPath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("================ 欢迎使用 全栈魔法师 Java 开发工具 ==================");
        System.out.println();
        System.out.println("Swagger/OpenAPI文档地址建议导入ApiFox或者postman使用: http://localhost:" + serverPort + apiDocsPath);
        System.out.println("Swagger/OpenApi Web 预览地址  http://localhost:" + serverPort +"/swagger-ui/index.html");
    }
}