package com.mandevices.iot.agriculture.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["id"]
)
data class SensorData(
        val id: Int,
        var content: String,
        val monitorTag:String,
        val year: Int,
        val month: Int,
        val day: Int) {
}