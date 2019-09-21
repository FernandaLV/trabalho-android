package br.com.fernandavedovello.trabalhofinal.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import br.com.fernandavedovello.trabalhofinal.R
import br.com.fernandavedovello.trabalhofinal.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val TEMPO_AGUARDANDO_SPLASHSCREEN = 3500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferences = getSharedPreferences("user_preference", Context.MODE_PRIVATE)
        val isFirstOpen = preferences.getBoolean("open_first", true)

        //if(isFirstOpen){
        if(true){
            markAppAlreadyOpen(preferences)
            showSplash()
        }
        else{
            showLogin()
        }
    }

    private fun markAppAlreadyOpen(preferences: SharedPreferences){
        val editor = preferences?.edit()
        editor?.putBoolean("open_first", false)
        editor?.apply()
    }

    fun showLogin(){
        val nextScreen = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(nextScreen)
        finish()
    }

    private fun showSplash(){
        //Carregando animação
        val anim = AnimationUtils.loadAnimation(this,R.anim.animacao_splash)
        anim.reset()
        ivLogo.clearAnimation()
        //Roda animação
        ivLogo.startAnimation(anim)
        //Chama a próxima tela após 3,5 segundos definidos na SPLASH_DISPLAY_LENGTH
        Handler().postDelayed({
            val nextScreen= Intent(this@SplashActivity,LoginActivity::class.java)
            startActivity(nextScreen)
            finish()
        }, TEMPO_AGUARDANDO_SPLASHSCREEN)
    }
}
