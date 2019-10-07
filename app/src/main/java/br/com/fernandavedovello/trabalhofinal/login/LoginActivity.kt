package br.com.fernandavedovello.trabalhofinal.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.fernandavedovello.trabalhofinal.MainActivity
import br.com.fernandavedovello.trabalhofinal.newuser.NewUserActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.text.TextUtils
import br.com.fernandavedovello.trabalhofinal.R

class LoginActivity : AppCompatActivity() {

    private lateinit var myAuth: FirebaseAuth

    private val newUserRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        myAuth = FirebaseAuth.getInstance()

        //Dá um reload para verificar se o usuário ainda existe
        //Caso não exista mais, pede o login novamente
        myAuth.currentUser?.reload()

        if(myAuth.currentUser != null){
            goToHome()
        }

        btnCreate.setOnClickListener{

            val strUserName = inputUser.text.toString()
            val strUserPassword = inputPassword.text.toString()

            if (TextUtils.isEmpty(strUserName)) {
                inputUser.error = getString(R.string.input_empty)
            }
            else if (TextUtils.isEmpty(strUserPassword)) {
                inputPassword.error = getString(R.string.input_empty)
            }
            else {
                myAuth.signInWithEmailAndPassword(
                    inputUser.text.toString(),
                    inputPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToHome()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            it.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        btnNewUser.setOnClickListener{
            startActivityForResult(
                Intent(
                    this,
                    NewUserActivity::class.java
                ),
                newUserRequestCode
            )
        }
    }

    private fun goToHome(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newUserRequestCode && resultCode == Activity.RESULT_OK){
            inputUser.setText(data?.getStringExtra("email"))
        }
    }

}
