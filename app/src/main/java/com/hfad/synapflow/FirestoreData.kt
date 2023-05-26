package com.hfad.synapflow

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.HashMap

class FirestoreData {
    private var userExists = false
    private lateinit var db : FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var synap: String
    private var completionDates  = mutableMapOf<String, Any>()
    private var taskMap = mutableMapOf<String, Task>()

    constructor() {
        db = FirebaseFirestore.getInstance()
        if (TESTMODE){
            uid = "tester"
        } else {
            fbAuth = FirebaseAuth.getInstance()
            uid = fbAuth.uid.toString()
        }

        println("UID IS : ${uid}")
        synap = "synapflow"
        checkUserExists()
    }

    public fun getUserExists() : Boolean  {
        return userExists
    }
    public fun checkUserExists(){
        val userRef = db.collection(synap).document(uid).collection("General").document("CompletionCount")
        userRef.get()
            .addOnSuccessListener { doc ->
                if (doc.getData() == null) {
                    println(doc)
                    println("Doc not found, creating user...")
                    createUser()
                } else {
                    println("Doc Found!")
                    userExists = true
                    updateCompletionDates(completionDates)
                    getTaskMap()
                }
            }
            .addOnFailureListener { doc ->
                println("err")
            }
    }

    /* Insert data */

    /**
     * Adds a task to the database, under the current user
     */
     fun addTask(task: Task) {
        val data: MutableMap<String, Any?> = HashMap()
        data["name"] = task.name
        data["description"] = task.description
        data["category"] = task.category
        data["startTimeStamp"] = task.startTimeStamp
        data["priority"] = task.priority
        data["completed"] = false

        // Add task to Tasks subcollection of user with a random id
        db.collection(synap).document(uid)
            .collection("Tasks").document()
            .set(data)
            .addOnSuccessListener {
                Log.d("dbfirebase", "save ${data}\n\n\n")
            }
            .addOnFailureListener {
                Log.d("dbfirebase", "FAIL ${data}\n\n\n")
            }
    }

    /* Update Variables */
    public fun onTimerCompletion() {
        /*
        Handles all logic for communicating to firestore and updating variables
        Once a study timer session has finished.
         */

        // Increment that a study cycle has completed.
        db.collection(synap).document(uid)
            .collection("General").document("CompletionCount")
            .update("CompletionCount", FieldValue.increment(1))

        // Todays Date
        val dateKey = LocalDate.now().toString()
        // Current time.
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        // Add the timers completion time to the DB
        db.collection(synap).document(uid)
            .collection("General").document("listOfCompletedSessions")
            .update(dateKey.toString(), FieldValue.arrayUnion(now))

        plots.refreshData()
    }

    public fun getCompletionDates() : MutableMap<String, Any> {
        updateCompletionDates(completionDates)
        return completionDates
    }
    public fun getTaskMap() : MutableMap<String, Task> {
        getTasksAsMap(taskMap)
        return taskMap
    }
    private fun resetTaskMap() {
        taskMap = mutableMapOf<String, Task>()
        getTasksAsMap(taskMap)
    }


    public fun updateCompletionDates(dataMap : MutableMap<String, Any>) {
        //var dataMap :  MutableMap<String, Any> = HashMap()
        db.collection(synap).document(uid)
            .collection("General").document("listOfCompletedSessions")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (data in document.data?.toMutableMap()!!) {
                        dataMap[data.key] = data.value
                    }
                println(dataMap)
                } else {
                    Log.d("dbfirebase", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("dbfirebase", "get failed with ", exception)
            }
    }
    fun getTasksAsMap(tasks : MutableMap<String, Task>){
        db.collection(synap).document(uid)
            .collection("Tasks")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val task = Task(
                        document.data["name"].toString(),
                        document.data["description"].toString(),
                        document.data["category"].toString(),
                        document.data["startTimeStamp"] as Timestamp,
                        document.data["priority"] as Long,
                        document.data["completed"] as Boolean,
                        document.id
                    )
                    tasks[document.id] = task
                }
            }
            .addOnFailureListener { exception ->
                Log.w("dbfirebase", "Error getting documents: ", exception)
            }
    }

    fun getCompletionCount(completion: (Long) -> Unit) {
        db.collection(synap).document(uid)
            .collection("General").document("CompletionCount")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val completionCount = document.getLong("CompletionCount")
                    completionCount?.let {
                        completion(it)
                    }
                } else {
                    Log.d("dbfirebase", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("dbfirebase", "get failed with ", exception)
            }
    }

    // Update existing task
    fun updateTask (task: Task) {
        val data: MutableMap<String, Any?> = HashMap()
        data["name"] = task.name
        data["description"] = task.description
        data["category"] = task.category
        data["startTimeStamp"] = task.startTimeStamp
        data["priority"] = task.priority
        data["completed"] = task.completed

        db.collection(synap).document(uid)
            .collection("Tasks").document(task.getUID())
            .update(data)
            .addOnSuccessListener {
                Log.d("dbfirebase", "save ${data}\n\n\n")
            }
            .addOnFailureListener {
                Log.d("dbfirebase", "FAIL ${data}\n\n\n")
            }
    }


    public fun deleteTask(taskID: String) {
        db.collection(synap).document(uid)
            .collection("Tasks").document(taskID)
            .delete()
            .addOnSuccessListener {
                Log.d("dbfirebase", "Task Deleted")
                resetTaskMap()
            }
            .addOnFailureListener { Log.d("dbfirebase", "Task failed to delete.") }
    }

    fun markTaskCompleted(taskID: String) {
        val data: MutableMap<String, Any?> = HashMap()
        data["completed"] = true

        db.collection(synap).document(uid)
            .collection("Tasks").document(taskID)
            .update(data)
            .addOnSuccessListener {
                Log.d("dbfirebase", "save ${data}\n\n\n")
            }
            .addOnFailureListener {
                Log.d("dbfirebase", "FAIL ${data}\n\n\n")
            }
    }

    fun undoTaskCompleted(taskID: String) {
        val data: MutableMap<String, Any?> = HashMap()
        data["completed"] = false

        db.collection(synap).document(uid)
            .collection("Tasks").document(taskID)
            .update(data)
            .addOnSuccessListener {
                Log.d("dbfirebase", "save ${data}\n\n\n")
            }
            .addOnFailureListener {
                Log.d("dbfirebase", "FAIL ${data}\n\n\n")
            }
    }

    /* Get Data */

    /**
     * Get a single Task from the database by the uid
     */
    fun getTask(uid: String) : Task {
        var task = Task()
        db.collection(synap).document(uid)
            .collection("Tasks").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    task = Task(
                        document.data!!["name"].toString(),
                        document.data!!["description"].toString(),
                        document.data!!["category"].toString(),
                        document.data!!["startTimeStamp"] as Timestamp,
                        document.data!!["priority"] as Long,
                        document.data!!["completed"] as Boolean,
                        document.id
                    )
                    Log.d("dbfirebase", "No such document")
                } else {
                    Log.d("dbfirebase", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("dbfirebase", "get failed with ", exception)
            }
        return task
    }

    /**
     * Get all tasks from user and return as a list of Task objects
     */
    fun getTasks() : MutableList<Task> {
        val tasks = mutableListOf<Task>()

        db.collection(synap).document(uid)
            .collection("Tasks")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val task = Task(
                        document.data["name"].toString(),
                        document.data["description"].toString(),
                        document.data["category"].toString(),
                        document.data["startTimeStamp"] as Timestamp,
                        document.data["priority"] as Long,
                        document.data["completed"] as Boolean,
                        document.id
                    )
                    tasks.add(task)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("dbfirebase", "Error getting documents: ", exception)
            }
        return tasks
    }

    /* User Creation */

    private fun createUser(){
        initGlobalVariables()
    }

    private fun iterLoadDataset(collectionName : String, dataset: MutableMap<String, Any?>) {
        /*
            Reads in a dataset as a collections.document.collections with each dataset key
            Being the document for the file. Since there is no builtin function to do this
            We have to iterate over the dataset to add them individually so we'll see a schema like
            "SynapFlow(col)" -> "Uid(doc)" -> "Global(col)" -> "completedSessions(doc)" -> 0
            initialized in firebase.
            This is also optimal for having lists of objects.

         */
        for ((docName, docVal) in dataset) {
            db.collection(synap).document(uid)
                .collection(collectionName).document(docName)
                .set(hashMapOf(docName to docVal))
                .addOnSuccessListener {
                    Log.d("dbfirebase", "save ${dataset}\n\n\n")
                    updateCompletionDates(completionDates)
                    getTaskMap()
                }
                .addOnFailureListener {
                    Log.d("dbfirebase", "FAIL ${dataset}\n\n\n")
                }
        }
    }

    // Review adding data: https://firebase.google.com/docs/firestore/manage-data/add-data
    private fun initGlobalVariables(){
        /*
        Initializes the global/general variables we will be accessing accross the board, like
        Completed tasks and incremental values
         */
        val dataset: MutableMap<String, Any?> = HashMap()
        dataset["CompletionCount"] = 0
        dataset["listOfCompletedSessions"] = true
        iterLoadDataset("General", dataset)
    }

}