package duoc.cl.mibolsillo.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BolsilloApiAdapter {

  private static BolsilloApiService API_SERVICE;

  public static BolsilloApiService getApiService() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    String baseUrl = "http://lavegaonline.ml:90/mibolsillo/public/api/";
    if (API_SERVICE == null) {
      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(baseUrl)
              .addConverterFactory(GsonConverterFactory.create())
              .client(httpClient.build())
              .build();
      API_SERVICE = retrofit.create(BolsilloApiService.class);
    }
    return API_SERVICE;
  }


}
