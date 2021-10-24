package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var adaptor: RVCelebrity
    lateinit var rvMain: RecyclerView
    lateinit var addButton: Button
    lateinit var celebrityNameInput: EditText
    lateinit var updateButton: Button

    var celebrities = ArrayList<Celebrity>()
    lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rvMain = findViewById(R.id.rvMain)
        addButton = findViewById(R.id.btnAddCelebrity)
        celebrityNameInput = findViewById(R.id.etCelebrityName)
        updateButton = findViewById(R.id.btnGoToUpdate)

        adaptor = RVCelebrity(celebrities)
        rvMain.adapter = adaptor
        rvMain.layoutManager = LinearLayoutManager(this)

        showProgressDialog()
        getCelebrities()


        addButton.setOnClickListener {
            val intent = Intent(this,AddNewCelebrity::class.java)
            startActivity(intent)
        }

        updateButton.setOnClickListener {
            if(celebrityNameInput.text.isNotEmpty()){
                val name = celebrityNameInput.text.toString()
                val celebrity = celebrities.find { it.name?.lowercase() == name.lowercase()  }
                if(celebrity != null){
                    val intent = Intent(this, UpdateCelebrity::class.java)
                    intent.putExtra("pk", celebrity.pk)
                    intent.putExtra("name", celebrity.name)
                    intent.putExtra("taboo1", celebrity.taboo1)
                    intent.putExtra("taboo2", celebrity.taboo2)
                    intent.putExtra("taboo3", celebrity.taboo3)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "the name is not in the list", Toast.LENGTH_LONG).show()
                    celebrityNameInput.text.clear()
                }
            }else{
                Toast.makeText(this, "Please, enter name first", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getCelebrities(){
        if(celebrities.isNotEmpty()){
            celebrities.clear()
        }
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<List<Celebrity>> = apiInterface!!.getCelebrities()

        call?.enqueue(object : Callback<List<Celebrity>>{
            override fun onResponse(
                call: Call<List<Celebrity>>,
                response: Response<List<Celebrity>>
            ) {
                val response: List<Celebrity>? =response.body()
                for (celebrity in response!!){
                    celebrities.add(celebrity)
                }
                rvMain.adapter!!.notifyDataSetChanged()
                removeProgressDialog()
            }

            override fun onFailure(call: Call<List<Celebrity>>, t: Throwable) {
                Log.d("TAG", "${t}")
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                removeProgressDialog()
            }

        })
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, StartGame::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }
}