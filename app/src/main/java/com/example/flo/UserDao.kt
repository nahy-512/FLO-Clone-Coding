package com.example.flo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insert(user:User)

    @Query("SELECT * FROM UserTable")  //UserTable에 저장된 모든 정보를 가져올 수 있도록 하는 쿼리문
    fun getUsers() : List<User>

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password") //한 명의 유저에 대한 정보만 가져오는 쿼리문
    fun getUser(email:String, password:String) : User? //정보가 있을수도, 없을수도 있으니 null 처리
}