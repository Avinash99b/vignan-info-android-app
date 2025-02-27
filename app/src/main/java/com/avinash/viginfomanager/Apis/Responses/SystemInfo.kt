package com.avinash.viginfomanager.Apis.Responses

data class SystemInfo(
    val block_id: Int,
    val description: String,
    val download_speed: Int?,
    val id: Int,
    val incharge_id: String,
    val lab_id: Int,
    val name: String,
    val ping: Int?,
    val upload_speed: Int?,
    val working: Boolean,
    val floor: Int
)