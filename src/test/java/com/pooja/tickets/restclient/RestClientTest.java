package com.pooja.tickets.restclient;

import com.pooja.tickets.exceptions.AuthenticationException;
import com.pooja.tickets.exceptions.ResourceNotFoundException;
import org.apache.http.HttpResponseFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestClientTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse mockHttpResponse;

    private RestClient restClient;

    @Before
    public void setUp() {
        restClient = new RestClient("Abc", "123", mockHttpClient);
    }

    @Test
    public void getHttpResponse_happyCase() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        String expectedResponse = "Test";
        when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(expectedResponse);

        String url = "https://zccpooja.zendesk.com/";
        String actualResponse = restClient.getHttpResponse(url);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test(expected = AuthenticationException.class)
    public void getHttpResponse_authenticationFailure() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(401);
        String url = "https://zccpooja.zendesk.com/api/v2/tickets/1";
        restClient.getHttpResponse(url);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getHttpResponse_resourceNotFound() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(404);
        String url = "https://zccpooja.zendesk.com/api/v2/tickets/100000";
        restClient.getHttpResponse(url);
    }

}
