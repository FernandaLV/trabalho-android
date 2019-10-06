package br.com.fernandavedovello.trabalhofinal.newuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.fernandavedovello.trabalhofinal.R
import br.com.fernandavedovello.trabalhofinal.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_user.*
import com.google.firebase.database.FirebaseDatabase

class NewUserActivity : AppCompatActivity() {

    private lateinit var myAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        myAuth = FirebaseAuth.getInstance()

        btnCreate.setOnClickListener {

            if(validateFormData()) {

                myAuth.createUserWithEmailAndPassword(
                    inputEmail.text.toString(),
                    inputPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveInRealTimeDatabase()
                    } else {
                        Toast.makeText(
                            this@NewUserActivity,
                            it.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateFormData() :Boolean {
        val name = inputName.text.toString()
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()

        if (name == ""){
            Toast.makeText(
                this,
                "Preencha o nome",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (email== ""){
            Toast.makeText(
                this,
                "Preencha o e-mail",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (password== ""){
            Toast.makeText(
                this,
                "Preencha a senha",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun saveInRealTimeDatabase(){
        var user = User(
            inputName.text.toString(),
            inputEmail.text.toString()
        )

        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(
                        this,
                        "Usuário criado com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("email", inputEmail.text.toString())
                    setResult(RESULT_OK,returnIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Erro ao criar o usuário",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
