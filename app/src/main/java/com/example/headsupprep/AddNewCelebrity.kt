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

class AddNewCelebrity : AppCompatActivity() {

    lateinit var celebrityName: EditText
    lateinit var taboo1: EditText
    lateinit var taboo2: EditText
    lateinit var taboo3: EditText
    lateinit var addButton: Button
    lateinit var backButton: Button

    lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_celebrity)

        celebrityName = findViewById(R.id.etName)
        taboo1 = findViewById(R.id.etTaboo1)
        taboo2 = findViewById(R.id.etTaboo2)
        taboo3 = findViewById(R.id.etTaboo3)
        addButton = findViewById(R.id.btnAddNewCelebrity)
        backButton = findViewById(R.id.btnBack)

        addButton.setOnClickListener {
            showProgressDialog()
            if(celebrityName.text.isNotEmpty() && taboo1.text.isNotEmpty() && taboo2.text.isNotEmpty()
                && taboo3.text.isNotEmpty()){
                val celebrity = Celebrity()
                celebrity.name = celebrityName.text.toString()
                celebrity.taboo1 = taboo1.text.toString()
                celebrity.taboo2 = taboo2.text.toString()
                celebrity.taboo3 = taboo3.text.toString()

                val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
                apiInterface?.addNewCelebrity(celebrity)?.enqueue(object : Callback<Celebrity>{
                    override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                        Toast.makeText(this@AddNewCelebrity, "Added", Toast.LENGTH_LONG).show()
                        removeProgressDialog()
                        val intent = Intent(this@AddNewCelebrity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                        Toast.makeText(this@AddNewCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
                        removeProgressDialog()
                    }

                })
            }else{
                Toast.makeText(this, "Please, Enter Full Information", Toast.LENGTH_LONG).show()
            }
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun showProgressDialog(){
        progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Wait while loading...")
        progress.show()
    }
    fun removeProgressDialog(){
        progress.dismiss()
    }
}