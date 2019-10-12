package br.com.fernandavedovello.trabalhofinal.book

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import br.com.fernandavedovello.trabalhofinal.AppMenu
import br.com.fernandavedovello.trabalhofinal.R
import br.com.fernandavedovello.trabalhofinal.model.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_user.btnCreate
import kotlinx.android.synthetic.main.book_form.*

class NewBookActivity : AppMenu() {

    private lateinit var myAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_book)

        myAuth = FirebaseAuth.getInstance()

        btnCreate.setOnClickListener {
            val strUserName = inputTitle.text.toString()
            if (TextUtils.isEmpty(strUserName)) {
                inputTitle.error = getString(R.string.input_empty)
            }
            else {
                saveInRealTimeDatabase()
            }

        }

    }

    private fun saveInRealTimeDatabase(){

        val strNumberPages = inputNumberPages.text.toString()
        val strNumberPagesRead = inputNumberPagesRead.text.toString()

        val intNumberPages = when (TextUtils.isEmpty(strNumberPages)) {
            true -> 0
            false -> Integer.parseInt(strNumberPages)
        }

        val intNumberPagesRead = when (TextUtils.isEmpty(strNumberPagesRead)) {
            true -> 0
            false -> Integer.parseInt(strNumberPagesRead)
        }

        val book = Book(
            inputTitle.text.toString(),
            inputAuthor.text.toString(),
            intNumberPages,
            intNumberPagesRead
        )

        FirebaseDatabase.getInstance().getReference("Books")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .push()
            .setValue(book)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(
                        this,
                        getText(R.string.success_save),
                        Toast.LENGTH_SHORT
                    ).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("title", inputTitle.text.toString())
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
