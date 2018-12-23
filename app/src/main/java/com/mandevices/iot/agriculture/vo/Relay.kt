package com.mandevices.iot.agriculture.vo

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
        indices = [
            Index("id")],
        primaryKeys = ["id"]
)
data class Relay(
        val id: String,
        val serviceTag: String,
        val controlTag: String,
        val index:Int,
        val state:String,
        val name: String,
        val isRepeat:Boolean?,
        val onMinute:String?,
        val onHour:String?,
        val offMinute:String?,
        val offHour:String?
):Serializable