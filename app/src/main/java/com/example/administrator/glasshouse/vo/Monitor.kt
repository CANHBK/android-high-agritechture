package com.example.administrator.glasshouse.vo

import androidx.room.Entity

@Entity
data class Monitor(
       val id:String,
       val name:String,
       val tag:String

)