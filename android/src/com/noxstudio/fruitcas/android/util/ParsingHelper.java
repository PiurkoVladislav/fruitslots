package com.noxstudio.fruitcas.android.util;

import android.net.Uri;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ParsingHelper {

    private String url;
    private User result;

    private List<Results> mResults = new ArrayList<>();

    public ParsingHelper(String url) {
        this.url = url;

    }

    public User getResult(String country,int timeZone) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("country", country)
                .appendQueryParameter("tz", String.valueOf(timeZone))
                .build().toString();

        Request request = new Request.Builder()
                .url(uri)
                .get()
                .build();


        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            response.close();
            result = new User();

            JSONObject jsonBody = new JSONObject(json);
            result.setId(jsonBody.getInt("id"));
            result.setResult(jsonBody.getString("result"));
            Log.i("ParsingHelper","result: "+jsonBody.getString("result"));
            if(result.getResult().equals("")){
                return null;
            }else {
                return result;
            }
        } catch (JSONException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
            }
        return result;
    }


        public User getResultWithoutId(String country,int timeZone, int id) throws IOException {

            OkHttpClient client = new OkHttpClient();

            String uri = Uri.parse(url)
                    .buildUpon()
                    .appendQueryParameter("id", String.valueOf(id))
                    .appendQueryParameter("country", country)
                    .appendQueryParameter("tz", String.valueOf(timeZone))
                    .build().toString();

            Request request = new Request.Builder()
                    .url(uri)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                response.close();
                result = new User();

                JSONObject jsonBody = new JSONObject(json);
                result.setResult(jsonBody.getString("result"));
                result.setId(id);
                Log.i("ParsingHelper","result: "+jsonBody.getString("result"));
                if(result.getResult().equals("")){
                    return null;
                }else {
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


    public List<Results> getMasResult(int id) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(id))
                .build().toString();

        Request request = new Request.Builder()
                .url(uri)
                .get()
                .build();


        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            response.close();

            JSONArray photoJSONArray = new JSONArray(json);
            if(!json.equals("")) {
                for (int i = 0; i < photoJSONArray.length(); i++) {
                    JSONObject photoJsonObject = photoJSONArray.getJSONObject(i);
                    Results results = new Results();
                    results.setGoal(photoJsonObject.getInt("goal"));
                    results.setPayout(photoJsonObject.getInt("payout"));
                    mResults.add(results);
                    Log.i("ParsingHelper", "result: " + results.getGoal() + ", " + results.getPayout());
                }
                return mResults;
            }else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
