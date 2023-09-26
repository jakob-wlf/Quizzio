package de.firecreeper82.quizzio.data;

import com.google.gson.annotations.SerializedName;

public enum ResponseStatus {

    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR

}