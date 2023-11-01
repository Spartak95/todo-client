package com.apress.todo;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

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
        ResponseEntity<Iterable<ToDo>> response = restTemplate.exchange(requestEntity,
                                                                        new ParameterizedTypeReference<>() {
                                                                        });

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;
    }

    public ToDo findById(String id) {
        String url = properties.getUrl() + properties.getBasePath() + "/{id}";

        return restTemplate.getForObject(url, ToDo.class, Map.of("id", id));
    }

    public ToDo upsert(ToDo toDo) throws URISyntaxException {
        URI url = new URI(properties.getUrl() + properties.getBasePath());
        RequestEntity<?> requestEntity = new RequestEntity<>(toDo, POST, url);
        ResponseEntity<?> response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<ToDo>() {
        });

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return restTemplate.getForObject(response.getHeaders().getLocation(), ToDo.class);
        }

        return null;
    }

    public ToDo setCompleted(String id) {
        restTemplate.patchForObject(properties.getUrl() + properties.getBasePath() + "/{id}?_method=patch", null,
                                    ResponseEntity.class, Map.of("id", id));
        return findById(id);
    }

    public void delete(String id) {
        restTemplate.delete(properties.getUrl() + properties.getBasePath() + "/{id}", Map.of("id", id));
    }
}
