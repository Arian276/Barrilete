package com.bc.tvappvlc.model

data class Channel(
    val id: String,
    val name: String,
    val description: String,
    val logo: String,
    val url: String,
    val quality: String,
    val live: Boolean
)