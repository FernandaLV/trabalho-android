package br.com.fernandavedovello.trabalhofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.fernandavedovello.trabalhofinal.model.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_book.*
import kotlinx.android.synthetic.main.activity_new_user.btnCreate

class NewBookActivity : AppCompatActivity() {

    private lateinit var myAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_book)

        myAuth = FirebaseAuth.getInstance()

        btnCreate.setOnClickListener {
            saveInRealTimeDatabase()
        }

    }

    private fun saveInRealTimeDatabase(){
        var book = Book(
            inputTitle.text.toString()
        )

        var key = FirebaseDatabase.getInstance().getReference("Books").key



        FirebaseDatabase.getInstance().getReference("Books")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .push()
            .setValue(book)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(
                        this,
                        "Livro salvo com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("title", inputTitle.text.toString())
                    setResult(RESULT_OK,returnIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Erro ao salvar o livro",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
