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
        var lastTemp: Int=-1,
        var lastLight:Int=-1,
        var lastAirHumi:Int=-1,
        var lastGndHumi:Int=-1
) : Serializable