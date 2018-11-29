//package com.example.administrator.glasshouse.Model
//
//import com.example.administrator.glasshouse.AutoUpdatedEnvironmentSubSubscription
//import com.example.administrator.glasshouse.GetAllNodeEnvQuery
//
//data class SensorData(
//        var serviceTag: String,
//        var nodeEnv: String,
//        var name: String,
////        var temperatures: List<Any>?,
//        var temperatures: List<GetAllNodeEnvQuery.Temperature>?,
//        var lights: List<GetAllNodeEnvQuery.Light>?,
//        var airhummidities: List<GetAllNodeEnvQuery.AirHumidity>?,
//        var groundHumidities: List<GetAllNodeEnvQuery.GroundHumidity>?
////                      var pin: Int?
//)
//
//data  class RealTimeParam(
//        var serviceTag: String?,
//        var nodeEnv: String?,
//        var name: String?,
////        var temperatures: List<Any>?,
//        var temperatures: AutoUpdatedEnvironmentSubSubscription.Temperature?,
//        var lights: AutoUpdatedEnvironmentSubSubscription.Light?,
//        var airhummidities: AutoUpdatedEnvironmentSubSubscription.AirHumidity?,
//        var groundHumidities: AutoUpdatedEnvironmentSubSubscription.GroundHumidity?
////                      var pin: Int?
//)