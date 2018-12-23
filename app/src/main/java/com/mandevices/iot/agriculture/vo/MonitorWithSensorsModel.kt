package com.mandevices.iot.agriculture.vo

data class MonitorWithSensorsModel(
        val monitor:Monitor,
        var sensors:List<Sensor>?=null
) {
}