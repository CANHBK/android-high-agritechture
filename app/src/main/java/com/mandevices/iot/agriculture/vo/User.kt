package com.mandevices.iot.agriculture.vo

import androidx.room.Entity
import androidx.room.Index

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["email"]
)
data class User(
        val id: String,
        val fullName: String,
        val email: String
)