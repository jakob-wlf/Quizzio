package de.firecreeper82.quizzio.data;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @SerializedName("status")
    private ResponseStatus status;
    private String message;

    public SimpleResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}