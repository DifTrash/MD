package com.example.diftrash.data.retrofit

import com.google.gson.annotations.SerializedName

data class PostResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("confidence")
	val confidence: Any? = null,

	@field:SerializedName("type")
	val type: String? = null
)
