package com.avinash.viginfomanager.Apis.Responses

data class SystemInfo(
    val block_id: Int,
    val description: String,
    val download_speed: String,
    val floor: Int,
    val id: Int,
    val incharge_no: String,
    val keyboard_working: Boolean,
    val lab_id: Int,
    val mouse_working: Boolean,
    val name: String,
    val ping: String,
    val processor: String,
    val ram: String,
    val serial_no: String,
    val storage: String,
    val upload_speed: String,
    val working: Boolean
)