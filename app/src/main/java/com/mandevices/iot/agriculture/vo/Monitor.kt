package com.mandevices.iot.agriculture.vo

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["id"]
)
data class Monitor(
        val id: String,
        val name: String,
        val tag: String,
        val serviceTag: String,
        val lastTemp: Int=0,
        val lastLight:Int=0,
        val lastAirHumi:Int=0,
        val lastGndHumi:Int=0
) : Serializable