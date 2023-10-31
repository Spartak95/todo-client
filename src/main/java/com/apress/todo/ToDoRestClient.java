package com.apress.todo;

import static org.springframework.http.HttpMethod.GET;

import java.net.URI;
import java.net.URISyntaxException;

import com.apress.todo.domain.ToDo;
import com.apress.todo.error.ToDoErrorHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ToDoRestClient {
    private final RestTemplate restTemplate;
    private final ToDoRestClientProperties properties;

    public ToDoRestClient(ToDoRestClientProperties properties) {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new ToDoErrorHandler());
        this.properties = properties;
    }

    public Iterable<ToDo> findAll() throws URISyntaxException {
        URI url = new URI(properties.getUrl() + properties.getBasePath());
        RequestEntity<Iterable<ToDo>> requestEntity = new RequestEntity<>(GET, url);
        ResponseEntity<Iterable<ToDo>> response =
            restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;
    }
}
