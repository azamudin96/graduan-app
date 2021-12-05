package com.graduan.graduanapp.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.graduan.graduanapp.BuildConfig;

public class ServiceGenerator {
    public static String baseUrl = BuildConfig.ApiUrl;
    private static OkHttpClient.Builder httpClient;

    private static Retrofit.Builder builder;
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .create();

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("OKHttp", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson));


        httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(interceptor).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("android-test.graduan.info", session);
                }
            });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S CreateServiceByCustomURL(Class<S> serviceClass, String apiURL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("%%%% OKHttp", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder = new Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create(gson));


        httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(interceptor).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("android-test.graduan.info", session);
                }
            });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }


    public static <S> S createService(Class<S> serviceClass, final AccessToken token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("OKHttp", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor).build();

        if (BuildConfig.DEBUG) {
            System.out.println("okhttp , token getAccess_token = " + token.getToken());
        }
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson));

        if (token != null) {

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-type", "application/json")
                            .header("Authorization",
                                    token.getToken_type() + " " + token.getToken());
//                            .method(original.method(), original.body());

                    //TODO: session id for SSO
//                    if(token.getSession_id()!=null){
//                        requestBuilder.header("X-Session",token.getSession_id());
//                    }
                    requestBuilder.method(original.method(), original.body());


                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            }).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("android-test.graduan.info", session);
                }
            });
            if (BuildConfig.DEBUG)
                httpClient.addInterceptor(interceptor);

//            httpClient.authenticator(new Authenticator() {
//                @Override
//                public Request authenticate(Route route, Response response) throws IOException {
//                    if (responseCount(response) >= 1) {
//                        return null;
//                    }
//                    API tokenClient = createService(API.class);
//                    Call<AccessToken> call = tokenClient.refreshToken(token.getClient_id(), token.getClient_secret(), "refresh_token", "*", token.getRefresh_token());
//                    try {
//                        retrofit2.Response<AccessToken> tokenResponse = call.execute();
//                        if (tokenResponse.code() == 200) {
//                            AccessToken newToken = tokenResponse.body();
//                            AccountManager.getInstance(null).setToken(newToken);
//
//                            return response.request().newBuilder()
//                                    .header("Authorization", newToken.getToken_type() + " " + newToken.getAccess_token())
//                                    .header("X-Session",newToken.getSession_id())
//                                    .build();
//                        } else {
//                            return null;
//                        }
//                    } catch (IOException e) {
//                        return null;
//                    }
//                }
//            });
        }
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }


    //===================== Firebase Notification - Demo ================


    public static <S> S CreateFirebaseService(Class<S> serviceClass, String apiURL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("%%%% OKHttp", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor).build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create(gson));


        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-type", "application/json")
                        .header("Authorization",
                                "key=AIzaSyCgFe67tNoInnl5Fpf3sDUYqG7l5vax7eU");//TODO: Firebase Key, Demo Only

                requestBuilder.method(original.method(), original.body());


                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(interceptor);

        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
