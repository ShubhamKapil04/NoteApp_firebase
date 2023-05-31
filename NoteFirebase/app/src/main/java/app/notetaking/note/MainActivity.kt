package app.notetaking.note

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.notetaking.note.adapter.NoteAdapter
import app.notetaking.note.auth.LoginActivity
import app.notetaking.note.data.Note
import app.notetaking.note.data.Utility
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var addNoteBtn: FloatingActionButton
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var menuBtn: ImageButton
    private lateinit var adapter: NoteAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNoteBtn = findViewById(R.id.add_note_btn)
        notesRecyclerView = findViewById(R.id.recycler_view)
        menuBtn = findViewById(R.id.menu_btn)

        auth = Firebase.auth

        addNoteBtn.setOnClickListener {
            val intent = Intent(this, NoteDetailsActivity::class.java)
            startActivity(intent)
        }

        menuBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        //get notification collection  from noteDao
        //get current user Id
        val query = Utility().getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING)
        // To let user access his notes only
        // Create a firebaseRecyclerView Option
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Note>().setQuery(query, Note::class.java).build()
        //Now add adapter
        adapter = NoteAdapter(recyclerViewOption)
        notesRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

}
