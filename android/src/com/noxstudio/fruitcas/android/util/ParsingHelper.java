package com.noxstudio.fruitcas.android.util;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ParsingHelper {

    private String url;
    private User result;
    private Retrofit mRetrofit;
    private Gson mGson;
    private MyRetrofitInter mMyRetrofitInter;

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
        Log.e("PAAAAAAAAAAA",uri);
        Log.e("PAAAAAAAAAAA", String.valueOf(timeZone));


        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Log.e("PAAAAAAAAAAA", json);
            response.close();
            result = new User();

            JSONObject jsonBody = new JSONObject(json);
            result.setId(jsonBody.getInt("id"));
            result.setResult(jsonBody.getString("result"));
            Log.e("PAAAAAAAAAAA11111", jsonBody.getString("result"));
            Log.e("PAAAAAAAAAAA11111", String.valueOf(jsonBody.getInt("id")));
            Log.e("PAAAAAAAAAAA11111", result.getResult());
            return result;
        } catch (JSONException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
            }
        Log.e("PAAAAAAAAAAA11", result.getResult());
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
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


//        mGson = new GsonBuilder().create();
//
//        mRetrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create(mGson))
//                .build();
//
//        mMyRetrofitInter = mRetrofit.create(MyRetrofitInter.class);
//        Call<User> userCall;
//        if(id==0){
//            userCall = mMyRetrofitInter.getUrl(country,timeZone);
//            userCall.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    if(response.isSuccessful()) {
//                        result.setId(response.body().getId());
//                        result.setResult(response.body().getResult());
//                    }
//                }
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//                    result = null;
//                }
//            });
//        }
//        else {
//            userCall = mMyRetrofitInter.getUrlWithoutId(id,country, timeZone);
//            userCall.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    if(response.isSuccessful()) { ;
//                        result.setResult(response.body().getResult());
//                    }
//                }
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//                    result = null;
//                }
//            });
//        }




}
