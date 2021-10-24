package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewCelebrityLocally : AppCompatActivity() {

    lateinit var celebrityName: EditText
    lateinit var taboo1: EditText
    lateinit var taboo2: EditText
    lateinit var taboo3: EditText
    lateinit var addButton: Button
    lateinit var backButton: Button

    lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_celebrity_locally)

        celebrityName = findViewById(R.id.etNameLocally)
        taboo1 = findViewById(R.id.etTaboo1Locally)
        taboo2 = findViewById(R.id.etTaboo2Locally)
        taboo3 = findViewById(R.id.etTaboo3Locally)
        addButton = findViewById(R.id.btnAddNewCelebrityLocally)
        backButton = findViewById(R.id.btnBackLocally)

        addButton.setOnClickListener {
            showProgressDialog()
            if(celebrityName.text.isNotEmpty() && taboo1.text.isNotEmpty() && taboo2.text.isNotEmpty()
                && taboo3.text.isNotEmpty()){
                val celebrity = Celebrity()
                celebrity.name = celebrityName.text.toString()
                celebrity.taboo1 = taboo1.text.toString()
                celebrity.taboo2 = taboo2.text.toString()
                celebrity.taboo3 = taboo3.text.toString()

                val celebrityDBHelper = CelebrityDBHelper(applicationContext)
                val result = celebrityDBHelper.saveCelebrity(celebrity)

                if(result != -1L){
                    Toast.makeText(this, "Added", Toast.LENGTH_LONG).show()
                    removeProgressDialog()
                }else{
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    removeProgressDialog()
                }
            }else{
                Toast.makeText(this, "Please, Enter Full Information", Toast.LENGTH_LONG).show()
            }
        }
        backButton.setOnClickListener {
            val intent = Intent(this, StartGame::class.java)
            startActivity(intent)
        }

    }

    fun showProgressDialog(){
        progress = ProgressDialog(this)
        progress.setTitle("Adding")
        progress.setMessage("Wait while Adding...")
        progress.show()
    }
    fun removeProgressDialog(){
        progress.dismiss()
    }
}