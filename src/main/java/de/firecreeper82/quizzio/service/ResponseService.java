package de.firecreeper82.quizzio.service;

import com.google.gson.Gson;
import de.firecreeper82.quizzio.data.DataResponse;
import de.firecreeper82.quizzio.data.ResponseStatus;
import de.firecreeper82.quizzio.data.SimpleResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ResponseService {

    public String createJsonResponse(ResponseStatus status, String message) {
        SimpleResponse response = new SimpleResponse(status, message);
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    public String createJsonResponse(ResponseStatus status, HashMap<String, String> message) {
        DataResponse response = new DataResponse(status, message);
        Gson gson = new Gson();
        return gson.toJson(response);
    }

}