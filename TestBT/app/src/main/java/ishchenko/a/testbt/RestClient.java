package ishchenko.a.testbt;

import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrey on 07.03.17.
 */

public class RestClient {
    private static RestClient instance = null;
    private static String BASE_URL = "http://192.168.1.125:8000/";
    private API api;
    private Retrofit retrofit;
    private SharedPreferences sp;

    public static RestClient getInstance(SharedPreferences sp) {
        if (instance == null) {
            instance = new RestClient(sp);
        }
        return instance;
    }

    private RestClient(SharedPreferences sp) {
        this.sp = sp;
        BASE_URL = sp.getString("url", "default");
        buildClient();
    }

    public void rebuild() {
        buildClient();
    }

    private void buildClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Interceptor.Chain chain) throws IOException {
                                          Request original = chain.request();

                                          Request request = original.newBuilder()
                                                  .header("Authorization", sp.getString("token", "default"))
                                                  .method(original.method(), original.body())
                                                  .build();
                                          return chain.proceed(request);
                                      }
                                  });
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        api = retrofit.create(API.class);
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public API getApiService() {
        return this.api;
    }
}
