package com.example.administrator.glasshouse.api
import androidx.lifecycle.LiveData
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.User

interface  GraphQL  {
   fun loadGates(userId: String):LiveData<ApiResponse<List<Gate>>>
   fun login(email:String,password:String):LiveData<ApiResponse<User>>
   fun register(email:String,name:String,password:String):LiveData<ApiResponse<User>>
}
