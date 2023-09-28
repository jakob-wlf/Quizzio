package de.firecreeper82.quizzio.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class MailService {

    private final String apiKey;
    private final String secret;

    public MailService() throws FileNotFoundException {
        File myFile = new File("src/main/resources/apiCredentials.txt");
        Scanner myReader = new Scanner(myFile);

        apiKey = myReader.nextLine();
        secret = myReader.nextLine();

        myReader.close();
    }

    public void sendMail(
            String from,
            String fromName,
            String to,
            String toName,
            String subject,
            String body
    ) throws MailjetException {

        MailjetRequest request;
        MailjetResponse response;

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secret)
                .build();

        MailjetClient client = new MailjetClient(options);

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", from)
                                        .put("Name", fromName))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", to)
                                                .put("Name", toName)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.TEXTPART, body)
                                .put(Emailv31.Message.HTMLPART,
                                        "<h3>Quizzio</h3>" +
                                                "<br>" +
                                                body
                                )));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

}
