package com.mandevices.iot.agriculture.vo

import androidx.room.Entity
import androidx.room.Index

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["id"]
)
data class Gate(
        val id: String,
        val name: String,
        val monitors: String = "0",
        val controls: String = "0",
        val serviceTag:String,
        val owner:String

)
