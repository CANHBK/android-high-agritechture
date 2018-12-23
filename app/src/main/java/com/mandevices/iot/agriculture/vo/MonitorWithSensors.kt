package com.mandevices.iot.agriculture.vo

import androidx.room.Embedded
import androidx.room.Relation


class MonitorWithSensors
 {
    @Embedded
    var monitor: Monitor? = null

    @Relation(parentColumn = "tag", entityColumn = "tag")
    var sensorList: List<Sensor>? = null
}