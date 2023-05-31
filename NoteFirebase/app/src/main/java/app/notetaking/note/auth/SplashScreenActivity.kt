package app.notetaking.note.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import app.notetaking.note.MainActivity
import app.notetaking.note.R
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser

            if(currentUser == null){
                //User is not Login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                //User is Login
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)
    }
}