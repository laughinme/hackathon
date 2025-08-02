package com.example.hackathon.domain.model

data class ExchangeLocation(
    val id: Int,
    val title: String,
    val address: String,
    val city: City
)