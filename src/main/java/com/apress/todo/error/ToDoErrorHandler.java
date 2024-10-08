package com.apress.todo.error;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class ToDoErrorHandler extends DefaultResponseErrorHandler {
    private final Logger log = LoggerFactory.getLogger(ToDoErrorHandler.class);

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        log.error(response.getStatusCode().toString());
        log.error(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
    }
}
