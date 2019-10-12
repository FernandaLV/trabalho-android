package br.com.fernandavedovello.trabalhofinal

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.fernandavedovello.trabalhofinal.about.AboutActivity
import br.com.fernandavedovello.trabalhofinal.splash.SplashActivity
import com.google.firebase.auth.FirebaseAuth

abstract class AppMenu : AppCompatActivity() {

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
        R.id.action_about -> {

            val intent = Intent(this, AboutActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

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