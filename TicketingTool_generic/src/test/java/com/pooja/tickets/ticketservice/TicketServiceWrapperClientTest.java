package com.pooja.tickets.ticketservice;

import com.pooja.tickets.exceptions.AuthenticationException;
import com.pooja.tickets.exceptions.ResourceNotFoundException;
import com.pooja.tickets.restclient.RestClient;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceWrapperClientTest {

    @Mock
    private RestClient mockRestClient;

    private static final String dummyUri = "dummy/";

    private TicketServiceWrapperClient zendeskwrapper;

    @Before
    public void setUp() {
        zendeskwrapper = new TicketServiceWrapperClient(dummyUri,  mockRestClient);
    }

    @Test
    public void getAllTicketsList_Test() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        String jsonResponse = "{\"name\": \"name_test\"}";
        when(mockRestClient.getHttpResponse(dummyUri+"api/v2/tickets?page[size]=25")).thenReturn(jsonResponse);
        JSONObject actualResponse = zendeskwrapper.getAllTicketsList(Optional.empty());
        assertNotNull(actualResponse);
    }

    @Test(expected = AuthenticationException.class)
    public void getAllTicketsList_authenticationFailure() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        when(mockRestClient.getHttpResponse(dummyUri+"api/v2/tickets?page[size]=25")).thenThrow(AuthenticationException.class);
        JSONObject actualResponse = zendeskwrapper.getAllTicketsList(Optional.empty());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getAllTicketsList_resourceNotFoundFailure() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        when(mockRestClient.getHttpResponse(dummyUri+"api/v2/tickets?page[size]=25")).thenThrow(ResourceNotFoundException.class);
        JSONObject actualResponse = zendeskwrapper.getAllTicketsList(Optional.empty());
    }

    @Test
    public void getTicketDetails_Test() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        String jsonResponse = "{\"name\": \"name_test\"}";
        when(mockRestClient.getHttpResponse(dummyUri+"api/v2/tickets/"+"1")).thenReturn(jsonResponse);
        JSONObject actualResponse = zendeskwrapper.getTicketDetails("1");
        assertNotNull(actualResponse);
    }

    @Test(expected = AuthenticationException.class)
    public void getTicketDetailsTest_authenticationFailure() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        when(mockRestClient.getHttpResponse(dummyUri+"api/v2/tickets/"+"1")).thenThrow(AuthenticationException.class);
        JSONObject actualResponse = zendeskwrapper.getTicketDetails("1");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getTicketDetailsTest_resourceNotFoundFailure() throws AuthenticationException, IOException, ResourceNotFoundException, InterruptedException {
        when(mockRestClient.getHttpResponse(dummyUri+"api/v2/tickets/"+"1")).thenThrow(ResourceNotFoundException.class);
        JSONObject actualResponse = zendeskwrapper.getTicketDetails("1");
    }
}
