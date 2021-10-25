package com.example.headsupprep

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


class StartGame : AppCompatActivity() {

    private val celebrityDBHelper by lazy { CelebrityDBHelper(applicationContext) }

    lateinit var gameData: LinearLayout
    lateinit var name: TextView
    lateinit var taboo1: TextView
    lateinit var taboo2: TextView
    lateinit var taboo3: TextView
    lateinit var time: TextView
    lateinit var headsUpText: TextView
    lateinit var hintText: TextView
    lateinit var startGameAPI: Button
    lateinit var startGameLocally: Button
    lateinit var addNewCelebrityButton: Button

    val text = "Please Rotate Device"
    var celebrities = ArrayList<Celebrity>()
    var index = 0
    var usedList = arrayListOf<Int>()
    var doesStart = false
    var gameMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_actionbar)



        gameData = findViewById(R.id.llGameData)
        name = findViewById(R.id.gameTvName)
        taboo1 = findViewById(R.id.gameTvTaboo1)
        taboo2 = findViewById(R.id.gameTvTaboo2)
        taboo3 = findViewById(R.id.gameTvTaboo3)
        gameData.isVisible = false

        time = findViewById(R.id.tvTime)
        headsUpText = findViewById(R.id.tvHeadsUp)
        hintText = findViewById(R.id.tvHint)
        startGameAPI = findViewById(R.id.btnStartGameAPI)
        startGameLocally = findViewById(R.id.btnStartGameLocally)
        addNewCelebrityButton = findViewById(R.id.btnAddNewCelebrityLocally)

        startGameAPI.setOnClickListener {
            gameMode = "api"
            getCelebrities()
            startTimer()
            headsUpText.text = text
            startGameAPI.isVisible = false
            startGameLocally.isVisible = false
            addNewCelebrityButton.isVisible = false
            hintText.isVisible = false
        }

        startGameLocally.setOnClickListener {
            gameMode = "locally"
            getCelebrities()
            startTimer()
            headsUpText.text = text
            startGameAPI.isVisible = false
            startGameLocally.isVisible = false
            addNewCelebrityButton.isVisible = false
            hintText.isVisible = false
        }

        addNewCelebrityButton.setOnClickListener { startActivity(Intent(this, AddNewCelebrityLocally::class.java)) }
    }


    private fun getCelebrities(){
        if(celebrities.isNotEmpty()){
            celebrities.clear()
        }
        if(gameMode == "api"){
            val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
            val call: Call<List<Celebrity>> = apiInterface!!.getCelebrities()

            call?.enqueue(object : Callback<List<Celebrity>> {
                override fun onResponse(
                    call: Call<List<Celebrity>>,
                    response: Response<List<Celebrity>>
                ) {
                    val response: List<Celebrity>? =response.body()
                    for (celebrity in response!!){
                        celebrities.add(celebrity)
                    }
                    gameMode = ""
                }

                override fun onFailure(call: Call<List<Celebrity>>, t: Throwable) {
                    Log.d("TAG", "${t}")
                    Toast.makeText(this@StartGame, "Something went wrong", Toast.LENGTH_LONG).show()
                }

            })
        }else if (gameMode == "locally"){
            celebrities = celebrityDBHelper.retrieveAllCelebrities()
            gameMode = ""
        }
    }

    private fun startTimer(){
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                time.text = "Time: " + millisUntilFinished / 1000
                doesStart = true
            }

            override fun onFinish() {
                time.text = "Time: done!"
                startGameAPI.isVisible = true
                startGameLocally.isVisible = true
                addNewCelebrityButton.isVisible = true
                headsUpText.isVisible = true
                hintText.isVisible = true
                headsUpText.text = "Heads Up!"
                gameData.isVisible = false
                usedList.clear()
                doesStart = false
            }
        }.start()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val orientation = newConfig.orientation
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            headsUpText.isVisible = true
            gameData.isVisible = false
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
             if(celebrities.size > 0){
                while (true){
                    index = Random.nextInt(celebrities.size)
                    if(!usedList.contains(index) || usedList.size == celebrities.size){
                        break
                    }
                }
                if(doesStart){
                    headsUpText.isVisible = false
                    val c = celebrities[index]
                    name.text = c.name
                    taboo1.text = c.taboo1
                    taboo2.text = c.taboo2
                    taboo3.text = c.taboo3
                    gameData.isVisible = true

                    if(usedList.size >= celebrities.size){
                        usedList.clear()
                    }else{
                        usedList.add(index)
                    }
                }
            }else{
                Toast.makeText(this,"Please Add Celebrities First",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.start_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }
}
