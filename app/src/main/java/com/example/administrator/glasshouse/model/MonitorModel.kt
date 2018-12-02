package com.example.administrator.glasshouse.model

import java.io.Serializable

data class MonitorModel(
        var serviceTag: String,
        var nodeEnv: String,
        var name: String
) : Serializable

