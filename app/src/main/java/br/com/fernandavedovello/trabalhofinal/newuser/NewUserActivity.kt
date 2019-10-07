package br.com.fernandavedovello.trabalhofinal.newuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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

        val strUserName = inputName.text.toString()
        val strEmail = inputEmail.text.toString()
        val strUserPassword = inputPassword.text.toString()

        if (TextUtils.isEmpty(strUserName)) {
            inputName.error = getString(R.string.input_empty)
            return false
        }
        if (TextUtils.isEmpty(strEmail)) {
            inputEmail.error = getString(R.string.input_empty)
            return false
        }
        if (TextUtils.isEmpty(strUserPassword)) {
            inputPassword.error = getString(R.string.input_empty)
            return false
        }

        return true
    }

    private fun saveInRealTimeDatabase(){
        val user = User(
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
                        getText(R.string.success_save),
                        Toast.LENGTH_SHORT
                    ).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("email", inputEmail.text.toString())
                    setResult(RESULT_OK,returnIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getText(R.string.fail_save),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
