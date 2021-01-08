package com.example.android.discoroll;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.google.gson.Gson;

import java.io.IOException;

public class HTTPAdapter {

    private static String apiEndpoint = "https://api.spotify.com/v1/";
    private static OkHttpClient client = new OkHttpClient();

    public static PseudoJson getAudioFeatures(String authToken, String trackId) {
        String requestUrl = apiEndpoint + "audio-features/" + trackId;
        Request req = new Request.Builder()
                .url(requestUrl)
                .header("Authorization", "Bearer " + authToken)
                .build();
        Log.d("HTTPAdapter", "Sending to " + requestUrl);

        PseudoJson result = new PseudoJson();
        Call call = client.newCall(req);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response){
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String responseStr = responseBody.string();
                    result.populateJson(responseStr);
                } catch (IOException e) {
                    Log.e("HTTPAdapter", e.getMessage(), e);
                } finally {
                    response.close();
                }
            }
        });

        return result;
    }
}
