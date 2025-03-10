package com.avinash.viginfomanager.Apis.Responses

data class System(
    val download_speed: String,
    val id: Int,
    val keyboard_working: Boolean,
    val lab_id: Int,
    val mouse_working: Boolean,
    val ping: String,
    val processor: String,
    val ram: String,
    val serial_no: String,
    val storage: String,
    val upload_speed: String,
    val working: Boolean
)