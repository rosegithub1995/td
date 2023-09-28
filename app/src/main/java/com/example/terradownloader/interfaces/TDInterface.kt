package com.example.terradownloader.interfaces

import com.example.terradownloader.model.TDPojo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://api-link-mauve.vercel.app/api/"

const val apiKey = ""

// Interface for your API
interface TDInterface {
    @GET("getTDLink/{dynamicPath}")
    fun getTdlink(@Path("dynamicPath") dynamicPath: String): Call<TDPojo>
}

// Singleton object for Retrofit
object TDService {
    val tdInstance: TDInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        tdInstance = retrofit.create(TDInterface::class.java)
    }
}
