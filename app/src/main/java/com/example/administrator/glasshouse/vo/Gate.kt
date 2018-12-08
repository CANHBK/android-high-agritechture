package com.example.administrator.glasshouse.vo

import androidx.room.Entity
import androidx.room.Index

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["serviceTag"]
)
data class Gate(
        val id: String,
        val name: String,
        val serviceTag: String,
        val monitors: String,
        val controls: String,
        val userId:String

)
