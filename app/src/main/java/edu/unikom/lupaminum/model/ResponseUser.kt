package edu.unikom.lupaminum.model

//gson serialize name

import com.google.gson.annotations.SerializedName

data class ResponseUser(
    @field:SerializedName("page")
    val page: Int? = null, //mapping parsing data dari retrofit

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("total_pages")
    val totalPage: Int? = null,

    @field:SerializedName("data")
    val data:List<DataItem>? = null
)
