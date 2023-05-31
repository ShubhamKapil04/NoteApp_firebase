package app.notetaking.note.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import app.notetaking.note.MainActivity
import app.notetaking.note.R
import app.notetaking.note.data.Utility
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    lateinit var loginAccountBtn: Button
    lateinit var progressBar: ProgressBar
    lateinit var createAccountBtnTextView: TextView

    //Firebase
    private val auth = FirebaseAuth.getInstance()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginAccountBtn = findViewById(R.id.login_btn)
        progressBar = findViewById(R.id.progress_bar)
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn)


        loginAccountBtn.setOnClickListener { v->
            loginUser()
        }

        createAccountBtnTextView.setOnClickListener { v->
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loginUser() {
        val email =  emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val isValidated = validation(email, password)

        if(!isValidated){
            return
        }

        loginAccountInFirebase(email, password)
    }

    private fun loginAccountInFirebase(email: String, password: String){
        changeInProgress(true)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            changeInProgress(false)
            if(task.isSuccessful){
                //Login is success
                if(auth.currentUser?.isEmailVerified!!){
                    //Go to mainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Utility().showToast(this, "Email not verified, Please verify your email")
                }
            }else{
                //login Failed
                task.exception?.localizedMessage?.let { Utility().showToast(this, it) }
            }
        }
    }
    private fun changeInProgress(inProgress: Boolean){
        if(inProgress){
            progressBar.visibility = View.VISIBLE
            loginAccountBtn.visibility = View.GONE
        }else{
            progressBar.visibility = View.GONE
            loginAccountBtn.visibility = View.VISIBLE
        }
    }


    private fun validation(email: String, password: String): Boolean {
        // Validation the data

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.error = "Email is invalid"
            return false
        }
        if(password.length < 6){
            passwordEditText.error = "Password length is invalid"
            return false
        }
        return true
    }
}