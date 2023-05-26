package com.hfad.synapflow

import com.google.firebase.Timestamp

class Task(
    var name: String,
    var description: String,
    var category: String,
    var startTimeStamp: Timestamp,
    var priority: Long,
    var completed: Boolean = false
) {

    private var uid: String = ""

    constructor() : this("", "", "", Timestamp.now(), 0, false) {
        // Empty constructor needed for Firestore serialization
    }

    // Constructor with uid
    constructor(name: String,
                description: String,
                category: String,
                startTimeStamp: Timestamp,
                priority: Long,
                completed: Boolean = false,
                uid: String) : this(name, description, category, startTimeStamp, priority, completed) {
     this.uid = uid
    }

    fun getUID(): String {
        return uid
    }
}