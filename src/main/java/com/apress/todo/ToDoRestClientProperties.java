package com.apress.todo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "todo")
public class ToDoRestClientProperties {
    private String url;
    private String basePath;
}
