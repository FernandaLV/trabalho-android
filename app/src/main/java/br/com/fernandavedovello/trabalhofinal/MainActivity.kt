package br.com.fernandavedovello.trabalhofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fernandavedovello.trabalhofinal.book.NewBookActivity
import br.com.fernandavedovello.trabalhofinal.model.Book
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val newBookActivityRequestCode = 1
    }

    private lateinit var mrecylerview : RecyclerView
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewBookActivity::class.java)
            startActivityForResult(intent, newBookActivityRequestCode)
        }

        this.ref = FirebaseDatabase.getInstance().getReference("Books")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        this.mrecylerview = findViewById<RecyclerView>(R.id.reyclerview)

        this.mrecylerview.layoutManager = LinearLayoutManager(this)

        firebaseData()

    }

    private fun firebaseData() {


        val option = FirebaseRecyclerOptions.Builder<Book>()
            .setQuery(ref, Book::class.java)
            .setLifecycleOwner(this)
            .build()

        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Book, BookViewHolder>(option) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
                val itemView = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.card_view,parent,false)
                return BookViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: BookViewHolder, position: Int, model: Book) {
                val placeid = getRef(position).key.toString()

                ref.child(placeid).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Error Occurred "+ p0.toException(), Toast.LENGTH_SHORT).show()

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        holder.title.setText(model.title)
                    }
                })
            }
        }

        this.mrecylerview.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }
}

class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var title = itemView.findViewById<TextView>(R.id.textView)
}
