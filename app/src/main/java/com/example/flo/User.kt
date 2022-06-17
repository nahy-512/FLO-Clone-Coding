package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "UserTable")
data class User(
    //사용자의 이메일, 패스워드 저장. 그에 따른 사용자 구별
    @SerializedName(value = "email") var email : String,
    @SerializedName(value = "password") var password : String,
    @SerializedName(value = "name") var name: String
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
