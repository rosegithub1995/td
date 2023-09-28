package com.example.terradownloader.interfaces

import com.example.terradownloader.model.TDPojo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api-link-mauve.vercel.app/api/"

const val apiKey = "";

//TO be implemneted in api
interface TDInterface {
    //API KEY GET Method
//    @GET("/getTDLink?api_key=${apiKey}")
//    fun getTdlink(@Query(surl) surl: String)
    @GET("/getTDLink")
    fun getTdlink(@Query("surl") surl: String): Call<TDPojo>


}

object TDService{
    val tdService:  TDInterface
    init {
        val retrofit= Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        tdService= retrofit.create(TDInterface::class.java);
    }
}