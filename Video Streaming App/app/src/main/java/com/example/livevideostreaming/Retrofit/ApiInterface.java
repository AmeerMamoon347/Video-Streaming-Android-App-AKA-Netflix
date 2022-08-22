package com.example.livevideostreaming.Retrofit;

import static com.example.livevideostreaming.Retrofit.RetrofitClient.BASE_URL;

import com.example.livevideostreaming.Models.AllCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET(BASE_URL)

    Observable<List<AllCategory>> getAllCategoriesMovies();

}
