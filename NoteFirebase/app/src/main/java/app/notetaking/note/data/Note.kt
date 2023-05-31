package app.notetaking.note.data

import com.google.firebase.Timestamp



class Note {
    var title: String? = null
    var content: String?= null
    lateinit var timestamp: Timestamp

    constructor() // no-argument constructor

    constructor(title: String?, content: String?, timestamp: Timestamp){
        this.title = title
        this.content = content
        this.timestamp = timestamp
    }
}
