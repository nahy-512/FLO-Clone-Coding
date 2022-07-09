package com.example.flo.data.remote

import android.util.Log
import com.example.flo.data.entities.User
import com.example.flo.ui.signin.LoginView
import com.example.flo.ui.signup.SignUpView
import com.example.flo.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun signUp(user : User){

        val authService = getRetrofit().create(AuthRetrofitinterface::class.java)

        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) { //응답이 왔을 때 해결하는 부분
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when(resp.code) {
                    1000-> signUpView.onSignupSuccess()
                    else-> signUpView.onSignUpFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) { //네트워크 연결이 실패했을 때 실행하는 부분
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }

        })
        Log.d("SIGNUP", "HELLO")
    }

    fun login(user : User){

        val authService = getRetrofit().create(AuthRetrofitinterface::class.java)

        authService.login(user).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) { //응답이 왔을 때 해결하는 부분
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> loginView.onLoginSuccess(code, resp.result!!)
                    else -> loginView.onLoginFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) { //네트워크 연결이 실패했을 때 실행하는 부분
                Log.d("LOGIN/FAILURE", t.message.toString())
            }

        })
        Log.d("LOGIN", "HELLO")
    }
}