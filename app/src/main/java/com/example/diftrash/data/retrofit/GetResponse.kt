package com.example.diftrash.retrofit

import com.google.gson.annotations.SerializedName

data class DyslexiaDataItem(
	val confidence: Any? = null,
	val diagnosis: String? = null,
	val id: String? = null
)

data class GetResponse(
	@field:SerializedName("dyslexia_data")
	val dyslexiaData: List<DyslexiaDataItem?>? = null
)

