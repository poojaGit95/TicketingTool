package com.pooja.tickets.restclient;
import com.google.common.annotations.VisibleForTesting;
import com.pooja.tickets.exceptions.AuthenticationException;
import com.pooja.tickets.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;


public class RestClient {

    String userName;
    String password;
    HttpClient client;

    public RestClient(String userId, String password){
        this.userName = userId;
        this.password = password;
        this.client = HttpClient.newHttpClient();
    }

    @VisibleForTesting
    public RestClient(String userId, String password, HttpClient httpClient){
        this.userName = userId;
        this.password = password;
        this.client = httpClient;
    }

    public String getHttpResponse(String url) throws IOException, InterruptedException, ResourceNotFoundException, AuthenticationException {
        String credentials = userName + ":" + password;
        String encodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes()));
        String actionUrl = url;

        // Create authorization header
        String authorizationHeader = "Basic " + encodedCredentials;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(actionUrl))
                .GET()
                .header("Authorization", authorizationHeader)
                .header("Content-Type", "application/json")
                .build();

        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 404 || response.statusCode() == 400) {
            throw new ResourceNotFoundException("Resource Not Found");
        } else if (response.statusCode() == 401) {
            throw new AuthenticationException("Authentication failed");
        }
        System.out.println(response);
        return response.body();
    }
}

