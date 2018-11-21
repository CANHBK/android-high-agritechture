package com.example.administrator.glasshouse.Model

data class SensorData(var nodeSensorID: String,
                      var nodeName: String,
                      var temp: String?,
                      var light: String?,
                      var airhummi: String?,
                      var groundHumi : String?,
                      var pin: Int?)