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

class UpdateCelebrity : AppCompatActivity() {

    lateinit var celebrityName: EditText
    lateinit var taboo1: EditText
    lateinit var taboo2: EditText
    lateinit var taboo3: EditText
    lateinit var updateButton: Button
    lateinit var deleteButton: Button
    lateinit var backButton: Button

    lateinit var progress: ProgressDialog
    var celebrity = Celebrity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_celebrity)

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        val extra = intent.extras
        if(extra != null){
            celebrity.pk = extra.getInt("pk")
            celebrity.name = extra.getString("name")
            celebrity.taboo1 = extra.getString("taboo1")
            celebrity.taboo2 = extra.getString("taboo2")
            celebrity.taboo3 = extra.getString("taboo3")
        }

        celebrityName = findViewById(R.id.etNameUpdated)
        celebrityName.setText(celebrity.name)

        taboo1 = findViewById(R.id.etTaboo1Updated)
        taboo1.setText(celebrity.taboo1)

        taboo2 = findViewById(R.id.etTaboo2Updated)
        taboo2.setText(celebrity.taboo2)

        taboo3 = findViewById(R.id.etTaboo3Updated)
        taboo3.setText(celebrity.taboo3)

        updateButton = findViewById(R.id.btnUpdate)
        deleteButton = findViewById(R.id.btnDelete)
        backButton = findViewById(R.id.btnBack)


        updateButton.setOnClickListener {
            showProgressDialog()
            celebrity.name = celebrityName.text.toString()
            celebrity.taboo1 = taboo1.text.toString()
            celebrity.taboo2 = taboo2.text.toString()
            celebrity.taboo3 = taboo3.text.toString()

            apiInterface?.updateCelebrity(celebrity.pk!!, celebrity)?.enqueue(object : Callback<Celebrity>{
                override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                    Toast.makeText(this@UpdateCelebrity, "Updated", Toast.LENGTH_LONG).show()
                    removeProgressDialog()
                    val intent = Intent(this@UpdateCelebrity, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                    Toast.makeText(this@UpdateCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
                    removeProgressDialog()
                }

            })
        }

        deleteButton.setOnClickListener {
            showProgressDialog()
            apiInterface?.deleteCelebrity(celebrity.pk!!)?.enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(this@UpdateCelebrity, "Deleted", Toast.LENGTH_LONG).show()
                    removeProgressDialog()
                    val intent = Intent(this@UpdateCelebrity, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@UpdateCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
                    removeProgressDialog()
                }

            })
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