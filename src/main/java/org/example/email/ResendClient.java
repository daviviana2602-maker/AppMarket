package org.example.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ResendClient {

    private final OkHttpClient client;
    private final String apiKey;
    private final ObjectMapper objectMapper;


    public ResendClient(OkHttpClient client,
                        @Value("${resend.api.key}") String apiKey,
                        ObjectMapper objectMapper
    )
    {
        this.client = client;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }


    public void sendEmail(String to, String subject, String html) {


        EmailRequest emailRequest = new EmailRequest(
                "noreply@vossodelivery.com.br",
                to,
                subject,
                html
        );


        String json;

        try {

            json = objectMapper.writeValueAsString(emailRequest);   // the ObjectMapper transforms Java object to JSON

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Failed to create email JSON", e);

        }


        RequestBody body = RequestBody.create(      // transforming json in the Body
                json,
                MediaType.get("application/json")
        );


        Request request = new Request.Builder()
                .url("https://api.resend.com/emails")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();


        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                throw new RuntimeException(
                        "Email error: " + response.body().string()
                );

            }

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

}
