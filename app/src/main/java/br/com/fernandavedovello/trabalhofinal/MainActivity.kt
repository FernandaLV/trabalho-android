package br.com.fernandavedovello.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fernandavedovello.trabalhofinal.book.EditBookActivity
import br.com.fernandavedovello.trabalhofinal.book.NewBookActivity
import br.com.fernandavedovello.trabalhofinal.model.Book
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_view.view.*

class MainActivity : AppMenu() {

    companion object {
        const val newBookActivityRequestCode = 1
        const val editBookActivityRequestCode = 2
    }

    private lateinit var mrecylerview : RecyclerView
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textNoBook.visibility = View.GONE

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewBookActivity::class.java)
            startActivityForResult(intent, newBookActivityRequestCode)
        }

        this.ref = FirebaseDatabase.getInstance().getReference("Books")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        this.mrecylerview = findViewById(R.id.reyclerview)

        this.mrecylerview.layoutManager = LinearLayoutManager(this)

        firebaseData()

        checkHasBooks()

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
                        Toast.makeText(
                            this@MainActivity,
                            "Error Occurred "+ p0.toException(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        holder.title.text = model.title
                        holder.id.text = placeid

                    }
                })
            }
        }

        this.mrecylerview.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }

    fun onClickBookItem(view: View){
        val intent = Intent(this@MainActivity, EditBookActivity::class.java)
            .putExtra("bookId", view.bookId.text.toString())
        startActivityForResult(intent, editBookActivityRequestCode)
    }

    private fun checkHasBooks() {
        val responseListener = object:ValueEventListener {
            override fun onDataChange(dataSnapshot:DataSnapshot) {
                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().currentUser!!.uid)) {
                    textNoBook.visibility = View.GONE
                }
                else {
                    textNoBook.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(databaseError:DatabaseError) {
            }
        }
        FirebaseDatabase.getInstance().getReference("Books").addValueEventListener(responseListener)
    }
}

class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var title = itemView.findViewById<TextView>(R.id.bookTitle)
    internal var id = itemView.findViewById<TextView>(R.id.bookId)
}

