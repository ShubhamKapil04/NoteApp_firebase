package app.notetaking.note

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import app.notetaking.note.data.Note
import app.notetaking.note.data.Utility
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class NoteDetailsActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var pageTitle: TextView
    private lateinit var contentEditText: EditText
    lateinit var saveNoteBtn: ImageButton
    lateinit var note : Note
    lateinit var title: String
    lateinit var content: String
    lateinit var docId: String
    private var isEditMode: Boolean = false
    private lateinit var deleteNoteTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        titleEditText = findViewById(R.id.note_title_text)
        contentEditText = findViewById(R.id.notes_content_text)
        saveNoteBtn = findViewById(R.id.save_note_btn)
        pageTitle = findViewById(R.id.page_title)
        deleteNoteTextView = findViewById(R.id.delete_note_text_view_btn)

        //Receive data
        title = intent.getStringExtra("title").toString()
        content = intent.getStringExtra("content").toString()
        docId = intent.getStringExtra("docId").toString()

        if(docId != null && docId.isNotEmpty()){
            isEditMode = true
        }

        titleEditText.setText(title)
        contentEditText.setText(content)

        if(isEditMode){
            pageTitle.text = "Edit Your Note"
            deleteNoteTextView.visibility = View.VISIBLE
        }


        saveNoteBtn.setOnClickListener { v-> saveNote() }

        deleteNoteTextView.setOnClickListener { v-> deleteNoteFromFirebase(docId) }
    }

    private fun saveNote() {
        val note = Note(
            title = titleEditText.text.toString(),
            content = contentEditText.text.toString(),
            timestamp = Timestamp.now()
        )

        saveNoteToFirestore(note)
    }

    private fun saveNoteToFirestore(note: Note) {
        val documentReference: DocumentReference

//        if(isEditMode){
            //update not
//            documentReference = Utility().getCollectionReferenceForNotes().document(docId)
//        }else{
            //create new mode
        documentReference = Utility().getCollectionReferenceForNotes().document()
//        }
        documentReference.set(note).addOnCompleteListener { task ->

            if(task.isSuccessful){
                //Note is added
                Utility().showToast(this, "Note added Successfully")
                finish()
            }else{
                //Note is not added
                Utility().showToast(this, "Failed while adding Note")
            }

        }
    }

    private fun deleteNoteFromFirebase(docId: String) {

            val db = FirebaseFirestore.getInstance()
            val noteCollection = db.collection("note")
//            val noteDocument = noteCollection.document(docIdD)

            noteCollection.document(docId).delete()
                .addOnCompleteListener {
                    Utility().showToast(this, "Note deleted Successfully")
                    finish()
                }
                .addOnFailureListener {
                    Utility().showToast(this, "Failed")
                }
    }
}

