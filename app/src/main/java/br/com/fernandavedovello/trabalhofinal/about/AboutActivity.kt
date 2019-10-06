package br.com.fernandavedovello.trabalhofinal.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.fernandavedovello.trabalhofinal.MainActivity
import br.com.fernandavedovello.trabalhofinal.R
import br.com.fernandavedovello.trabalhofinal.splash.SplashActivity
import com.google.firebase.auth.FirebaseAuth

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId){

        R.id.action_list -> {

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

            true
        }
        R.id.action_logout -> {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }

    }
}
