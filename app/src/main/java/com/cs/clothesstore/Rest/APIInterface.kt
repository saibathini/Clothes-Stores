package com.cs.clothesstore.Rest

import com.cs.clothesstore.Models.CatalougeResponse
import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {

    //    Country Code
    @GET("0f78766a6d68832d309d")
    fun getData(): Call<CatalougeResponse?>?
}