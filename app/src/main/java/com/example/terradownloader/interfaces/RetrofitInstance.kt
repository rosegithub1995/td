package com.example.terradownloader.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api-link-mauve.vercel.app/api/"

const val apiKey = ""

// Interface for your API
class RetrofitInstance {

    companion object {
        @Volatile
        private var tdInstance: Retrofit? = null
        fun getTDRetrofitInstance(): Retrofit {
            if (tdInstance == null) {
                // Synchronized is for making it thread safety.
                synchronized(this) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    tdInstance= retrofit

                }
            }
            return tdInstance!!
        }
    }
}

// Singleton object for Retrofit
