package br.com.fernandavedovello.trabalhofinal.book

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import br.com.fernandavedovello.trabalhofinal.AppMenu
import br.com.fernandavedovello.trabalhofinal.R
import br.com.fernandavedovello.trabalhofinal.model.Book
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_book.*
import kotlinx.android.synthetic.main.book_form.*


class EditBookActivity : AppMenu() {

    private lateinit var myAuth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var bookId: String
    private val firebaseReferenceNode = "Books"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        myAuth = FirebaseAuth.getInstance()

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                bookId = ""
            } else {
                bookId = extras.getString("bookId") ?: ""
            }
        } else {
            bookId = savedInstanceState.getSerializable("bookId") as String
        }

        listenerFirebaseRealtime()

        btnEdit.setOnClickListener{
            val strUserName = inputTitle.text.toString()
            if (TextUtils.isEmpty(strUserName)) {
                inputTitle.error = getString(R.string.input_empty)
            }
            else {
                saveBook()
            }
        }

        btnDelete.setOnClickListener{
            deleteBook()
        }

    }

    private fun listenerFirebaseRealtime() {

        if (FirebaseApp.getApps(this).size == 0) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }

        FirebaseDatabase.getInstance()
            .getReference(firebaseReferenceNode)
            .child(userId)
            .child(bookId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val book = dataSnapshot.getValue(Book::class.java)
                    inputTitle.setText(book?.title.toString())
                    inputAuthor.setText(book?.authorName.toString())
                    inputNumberPages.setText(book?.numberPages.toString())
                    inputNumberPagesRead.setText(book?.numberPagesRead.toString())
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun saveBook(){

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

        FirebaseDatabase.getInstance().getReference(firebaseReferenceNode)
            .child(userId)
            .child(bookId)
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

    private fun deleteBook(){
        FirebaseDatabase.getInstance().getReference(firebaseReferenceNode)
            .child(userId)
            .child(bookId)
            .removeValue()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(
                        this,
                        getText(R.string.success_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                    val returnIntent = Intent()
                    setResult(RESULT_OK,returnIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getText(R.string.fail_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
