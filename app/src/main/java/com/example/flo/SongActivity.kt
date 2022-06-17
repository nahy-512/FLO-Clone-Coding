package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    //소괄호 : 클래스를 다른 클래스로 상속을 진행할 때는 소괄호를 넣어줘야 한다.

    //전역 변수
    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayerList()
        initSong()
        initClickLestener()

    }

    private fun initPlayerList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickLestener(){
        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

   private fun initSong(){
       val spf = getSharedPreferences("song", MODE_PRIVATE)
       val songId = spf.getInt("song", 0)

       nowPos = getPlayingSongPosition(songId)

       Log.d("now Song ID", songs[nowPos].id.toString())

       startTimer()
       setPlayer(songs[nowPos])
   }

    //좋아요 버튼을 눌렀을 때 처리
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike //눌렀다면 반대값을 (DB값 업데이트 한 것 아님. DB값도 업데이트 해야 DB와 여기서 업데이트한 값이 동기화)
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        //새롭게 랜더링
        if (!isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private  fun moveSong(direct: Int){
        //다음노래: nowPos +1, 이전노래: nowPos -1
        if (nowPos + direct < 0){
            Toast.makeText(this, "First song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this, "Last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        //원래 진행하던 timer thread를 멈추고 ??nowPos에 대한 thread 재실행
        timer.interrupt()
        startTimer()

        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

        setPlayer(songs[nowPos]) //데이터 렌더링
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    //초기화된 song 정보 데이터 정보 렌더링
   private fun setPlayer(song: Song){
        binding.songMusicTitleIv.text = song.title
        binding.songSingerNameIv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song. second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song. playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        var music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        //보관함 song 데이터에 따라 좋아요 버튼 표시
        if (song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
   }

    fun setPlayerStatus(isPlaying : Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying
        mediaPlayer?.start()
        mediaPlayer?.pause()

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE //재생 버튼이 안 보이고
            binding.songPauseIv.visibility = View.VISIBLE  //정지 버튼이 보임
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE //재생 버튼이 보이고
            binding.songPauseIv.visibility = View.GONE  //정지 버튼이 안보임
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } //화면이 정지된 상태(false)
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true): Thread() {

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {

                while(true) {

                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }

                        //진행 시간 타이머
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }
            }catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다 ${e.message}")
            }
        }
    }
    //사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000 //SongActivity에서 재생되던 song의 데이터 Mini Player에 반영
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터

        editor.putInt("songId", songs[nowPos].id)

        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }
}