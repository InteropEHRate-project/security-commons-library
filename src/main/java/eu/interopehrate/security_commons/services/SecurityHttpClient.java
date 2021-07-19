package eu.interopehrate.security_commons.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SecurityHttpClient {

    public static String createPost(String url, String body) throws IOException, ExecutionException, InterruptedException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        HttpPost request = new HttpPost(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        request.setEntity(new StringEntity(body, "UTF-8"));
        Future<HttpResponse> future = client.execute(request, null);
        HttpResponse response = future.get();
        client.close();
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String result = entity != null ? EntityUtils.toString(entity) : null;
        if (status >= 200 && status < 300) {
            return result;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status + " " + result);
        }
    }


}
