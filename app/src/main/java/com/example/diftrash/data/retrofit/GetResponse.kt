package com.example.diftrash.data.retrofit

import com.google.gson.annotations.SerializedName

data class GetResponse(
	@field:SerializedName("data")
	val data: List<DataItem?>? = null,
)


data class DataItem(

	@field:SerializedName("confidence")
	val confidence: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
