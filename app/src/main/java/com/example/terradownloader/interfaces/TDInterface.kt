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
    suspend fun getTdlink(@Path("dynamicPath") dynamicPath: String): Call<TDPojo>

    companion object {
        @Volatile
        private var tdInstance: TDInterface? = null
        fun getTDRetrofitInstance(): TDInterface {
            if (tdInstance == null) {
                // Synchronized is for making it thread safety.
                synchronized(this) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    tdInstance = retrofit.create(TDInterface::class.java)
                }
            }
            return tdInstance!!
        }
    }
}

// Singleton object for Retrofit
