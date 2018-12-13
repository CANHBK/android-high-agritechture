package com.example.administrator.glasshouse.vo

import androidx.room.Entity
import androidx.room.Index

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["id"]
)
data class Monitor(
        val id: String,
        val name: String,
        val tag: String,
        val serviceTag: String
)