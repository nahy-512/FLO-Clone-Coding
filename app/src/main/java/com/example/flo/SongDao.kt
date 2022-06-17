package com.example.flo

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    //SongTable 매개변수에 넘겨준 isLike값을 업데이트
    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id")
    fun updateIsLikeById(isLike: Boolean,id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getlikedSongs(isLike: Boolean): List<Song>
}