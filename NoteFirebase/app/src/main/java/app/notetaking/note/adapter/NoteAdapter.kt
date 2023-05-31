package app.notetaking.note.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.notetaking.note.NoteDetailsActivity
import app.notetaking.note.R
import app.notetaking.note.data.Note
import app.notetaking.note.data.Utility
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NoteAdapter(options: FirestoreRecyclerOptions<Note>) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(
    options
){

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val noteTitle: TextView = itemView.findViewById(R.id.title_text_view)
        val noteContent: TextView = itemView.findViewById(R.id.content_text_view)
        val noteTimestamp: TextView = itemView.findViewById(R.id.timestamp_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
        holder.noteTitle.text = model.title
        holder.noteContent.text = model.content.toString()
        holder.noteTimestamp.text = Utility().timeStampToString(model.timestamp)
//
//        holder.itemView.setOnClickListener { v->
//            val intent = Intent(v.context, NoteDetailsActivity::class.java)
//            intent.putExtra("title", model.title)
//            intent.putExtra("content", model.content)
//            val docId: String = this.snapshots.getSnapshot(position).id
//            intent.putExtra("decId", docId)
//            v.context.startActivities(arrayOf(intent))
//        }
    }

}
