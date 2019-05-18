package com.philip.nytimessearch.mvvm.model.remote.response_model;

import android.util.Log;

import java.io.IOException;

import androidx.annotation.Nullable;
import retrofit2.Response;

/**
 * Wrapper class to contain Retrofit Response.
 */
public class ApiResponse<T> {
    private static final String LOG_TAG = ApiResponse.class.getSimpleName();

    private final int code;
    @Nullable
    public final T body;
    @Nullable
    public final String errorMessage;

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Log.e(LOG_TAG, "error while parsing response", ignored);
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    /**
     * Let's say only code between 200 and 300 is successful.
     */
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
}
