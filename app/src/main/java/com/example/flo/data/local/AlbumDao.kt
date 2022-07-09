package com.example.flo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like

@Dao
interface AlbumDao {

    @Insert
    fun insert(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums():List<Album>

    @Insert //사용자가 좋아요한 데이터 추가
    fun likeAlbum(like: Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId") //사용자가 앨범에 좋아요를 눌렀는지, 안 눌렀는지 확인
    fun isLikedAlbum(userId:Int, albumId:Int) : Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId") //좋아요를 취소 쿼리문
    fun disLikedAlbum(userId:Int, albumId:Int) //어떤 사용자가 어떤 앨범 좋아요 취소?

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")//좋아요한 앨범을 가져옴
    fun getLikedAlbums(userId: Int) : List<Album>
}