package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentStorageBinding

class StorageFragment() : Fragment() {

    lateinit var binding: FragmentStorageBinding
//    private var storageDatas = ArrayList<Storage>()
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageBinding.inflate(inflater,container,false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root

//        // 데이터 리스트 생성 더미 데이터 (arraylist에 담길 데이터)
//        storageDatas.apply {
//            add(Storage("Butter","방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Storage("Lilac","아이유 (IU)", R.drawable.img_album_exp2))
//            add(Storage("Next Level","에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Storage("Boy with Luv","방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Storage("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Storage("Weekend","태연 (Tae Yeon)", R.drawable.img_album_exp6))
//        }
//
//        val storageRVAdapter = StorageRVAdapter(storageDatas)
//        binding.storageStoreMusicAlbumRy.adapter = storageRVAdapter
//        binding.storageStoreMusicAlbumRy.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.storageStoreMusicAlbumRy.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = StorageRVAdapter() //SavedSongRVAdapter()

        songRVAdapter.setMyItemClickListener(object : StorageRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) { //클릭 리스너 구체화, DB 업데이트
                songDB.songDao().updateIsLikeById(false,songId)
            }

        })

        binding.storageStoreMusicAlbumRy.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getlikedSongs(true) as ArrayList<Song>)
    }

}