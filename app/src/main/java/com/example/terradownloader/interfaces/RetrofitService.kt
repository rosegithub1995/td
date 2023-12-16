package com.example.terradownloader.interfaces

import com.example.terradownloader.model.TDPojo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    @GET("getTDLink/{dynamicPath}")
    suspend fun getTdlink(@Path("dynamicPath") dynamicPath: String): Call<TDPojo>
}
