package com.avinash.viginfomanager.Apis.Responses

data class Block(
    val description: String,
    val id: Int,
    val image_url: String?,
    val name: String,
    val floors:Int
)