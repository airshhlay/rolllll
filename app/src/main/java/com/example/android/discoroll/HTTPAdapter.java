package com.example.android.discoroll;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Base64;

public class HTTPAdapter {
    // fix for authentication issues
    private static final String CLIENT_ID = "ab9a26a16bde446a87940a4203e3b808";
    private static final String SECRET_KEY = "7fe24a6c3dfc4e5b8c68a88caf54a8e7";
    private static final String AUTH_ENDPOINT = "https://accounts.spotify.com/api/token";
    private static String USER_TOKEN;

    private static final String apiEndpoint = "https://api.spotify.com/v1/";
    private static OkHttpClient client = new OkHttpClient();

    public static PseudoJson getAudioFeatures(String trackId) {
        String requestUrl = apiEndpoint + "audio-features/" + trackId;
        Request req = new Request.Builder()
                .url(requestUrl)
                .header("Authorization", "Bearer " + USER_TOKEN)
                .build();
        Log.d("HTTP", "Sending to " + requestUrl);

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
                    Log.d("HTTP", "Data Received.");
                    String responseStr = responseBody.string();
                    result.populateJson(responseStr);
                } catch (IOException e) {
                    Log.e("HTTP", e.getMessage(), e);
                } finally {
                    response.close();
                }
            }
        });

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void clientAuth(){
        String authToken = CLIENT_ID + ":" + SECRET_KEY;
        String encodedToken = Base64.getEncoder().encodeToString(authToken.getBytes());

        RequestBody reqBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request req = new Request.Builder()
                .url(AUTH_ENDPOINT)
                .addHeader("Authorization", "Basic " + encodedToken)
                .post(reqBody)
                .build();

        Call call = client.newCall(req);
        PseudoJson result = new PseudoJson();

        Log.d("HTTP", "Queued");
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
                    USER_TOKEN = result.getValue("access_token");
                    Log.d("HTTP", "Access Token Received.");

                } catch (IOException e) {
                    Log.e("HTTP", e.getMessage(), e);
                } finally {
                    response.close();
                }
            }
        });
    }
}
