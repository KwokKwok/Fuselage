package com.kwokstudio.fuselage.http;


import com.kwokstudio.fuselage.bean.Hito;
import com.kwokstudio.fuselage.bean.TextContent;
import com.kwokstudio.fuselage.bean.ZhihuThemeItem;
import com.kwokstudio.fuselage.config.UrlConfigs;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by 郭垒 on 2016/11/15.
 */

public class RetrofitCall {

    private static Retrofit mRetrofit;
    private static Retrofit hitoRetrofit;

    public static Retrofit getRetrofit(){
        if (mRetrofit==null){
            mRetrofit=new Retrofit.Builder()
                    .baseUrl(UrlConfigs.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static Retrofit getHito(){
        if (hitoRetrofit==null){
            hitoRetrofit=new Retrofit.Builder()
                    .baseUrl(UrlConfigs.HITO)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return hitoRetrofit;
    }

    public static RetrofitCall.ApiStore getApiStore(){
        return getRetrofit().create(ApiStore.class);
    }

    public static RetrofitCall.ApiStore getHitoStore(){
        return getHito().create(ApiStore.class);
    }

    public interface ApiStore{
        @GET("theme/{category}")
        Call<ZhihuThemeItem> getZhihuThemeItems(@Path("category") int category);
        @GET("news/{id}")
        Call<TextContent> getTextContent(@Path("id") int id);
        @GET("sp")
        Call<Hito> getMoreHito();


//        @GET("themes")
//        Call<ZhihuThemes> getZhihuThemes();



//        @GET("game?aid=androidhd1&client_sys=android&time=1476848726&auth=33060403dfce2403b874dd4c73c14fbc")
//        Call<Game> getGames();
//
//        @GET("live?aid=androidhd1&client_sys=android&")
//        Call<Live> getLives(@Query("limit") int num);
//
//        @GET("searchNew/{key}/{list}?aid=androidhd1&client_sys=android&")
//        Call<SearchDetailBean> getSearchDetail(@Path("key") String key, @Path("list") int list, @Query("limit") int num);
//
//        @GET("live/{category}?aid=androidhd1&client_sys=android&")
//        Call<Live> getCategoryLives(@Path("category") String category, @Query("limit") int num);
    }

}
