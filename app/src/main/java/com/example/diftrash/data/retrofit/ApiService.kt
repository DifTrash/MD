package com.example.diftrash.retrofit

import com.example.diftrash.data.retrofit.GetResponse
import com.example.diftrash.data.retrofit.PostResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


import retrofit2.http.Path

interface ApiService {

    @Multipart
    @POST("/predict/model/{uid}")
    suspend fun uploadImage(
        @Path("uid") uid: String,
        @Part file: MultipartBody.Part,
    ): PostResponse

    @POST("/predict/get/{uid}")
    fun getPrediction(
        @Path("uid") uid: String
    ): Call<GetResponse>


}
