package com.mandevices.iot.agriculture.vo

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["id"]
)
data class Sensor(
        val id: String,
        val name: String,
        val index:Int,
        val tag: String,
        val serviceTag: String,
        var isAuto:Boolean=true,
        val minute:String?,
        val hour:String?,
        val sensorID:String
):Serializable