package com.pooja.tickets.zendesk;

import com.google.common.annotations.VisibleForTesting;
import com.pooja.tickets.exceptions.AuthenticationException;
import com.pooja.tickets.exceptions.ResourceNotFoundException;
import com.pooja.tickets.restclient.RestClient;
import org.json.JSONObject;

import java.util.Optional;


public class ZendeskWrapperClient {
    RestClient client;
    String baseURI;

    public ZendeskWrapperClient(String baseURI, String username, String password){
        this.baseURI = baseURI;
        client = new RestClient(username, password);
    }

    @VisibleForTesting
    public ZendeskWrapperClient(String baseURI, RestClient client){
        this.baseURI = baseURI;
        this.client = client;
    }

    public JSONObject getAllTicketsList(Optional<String> urlParam) throws ResourceNotFoundException, AuthenticationException {
        String defaultUrl = baseURI+"api/v2/tickets?page[size]=25";
        String url = urlParam.orElse(defaultUrl);

        try {
            String response = client.getHttpResponse(url);
            JSONObject jsonObject = new JSONObject(response);
            return  jsonObject;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Tickets not found");
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONObject getTicketDetails(String id) throws ResourceNotFoundException, AuthenticationException {
        String url = baseURI+"api/v2/tickets/"+id;

        try {
            String response = client.getHttpResponse(url);
            JSONObject jsonObject = new JSONObject(response);
            return  jsonObject;

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Tickets not found");
        }  catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
