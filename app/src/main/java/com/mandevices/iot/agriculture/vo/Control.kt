package com.mandevices.iot.agriculture.vo

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["id"]
)
data class Control(
        val id: String,
        val name: String,
        val tag: String,
        val serviceTag: String
):Serializable