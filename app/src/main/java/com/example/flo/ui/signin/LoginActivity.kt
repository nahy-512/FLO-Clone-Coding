package com.example.flo.ui.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.ui.signup.SignUpActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.data.remote.Result
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.ui.main.MainActivity

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginLoginTv.setOnClickListener {
            login()
        }

    }

    //로그인 관련 함수
    private fun login(){
        if (binding.loginIdEt.text.toString().isEmpty() ||  binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPwdEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val password: String = binding.loginPwdEt.text.toString()

/*        //DB 정보 확인
        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email,pwd) //사용자 입력과 일치하는 정보가 DB에 있는지 확인

        user?.let {
            Log.d("LOGIN_ACT/GET_USER", "userId : ${user.id}, $user")
            //saveJwt(user.id)

            startMainActivity()
        }

 */     val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email,password,""))

        Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    //로그인 후 메인 화면으로 넘어가는 함수
    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

//    //JWT를 저장해주는 합수
//    private fun saveJwt(jwt:Int) {
//        val spf = getSharedPreferences("auth", MODE_PRIVATE)
//        val editor = spf.edit()
//
//        editor.putInt("jwt",jwt)
//        editor.apply()
//    }

    //JWT를 저장해주는 합수
    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt",jwt)
        editor.apply()
    }



    override fun onLoginSuccess(code : Int, result: Result) {
        when(code){
            1000 -> {
                saveJwt2(result.jwt)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        //실패처리
    }
}
