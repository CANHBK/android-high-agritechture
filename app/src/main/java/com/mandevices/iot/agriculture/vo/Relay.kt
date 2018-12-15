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
        val name: String,
        val index:Int,
        val controlTag: String,
        val serviceTag: String,
        val isPeriodic:Boolean,
        val minute:String?,
        val hour:String?,
        val state:String
):Serializable