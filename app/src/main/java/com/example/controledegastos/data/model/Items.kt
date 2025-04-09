package com.example.controledegastos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Items(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val description: String,
    val observation: String,
    val io: String? = null,
    val paymentMethod: String? = null,
    val value: Double,
    val date: String,
    val monthNumber: String? = null,
    val category: String? = null
)
