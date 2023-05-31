package app.notetaking.note.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import app.notetaking.note.R
import app.notetaking.note.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    lateinit var confirmEditText: EditText
    lateinit var createAccountButton: Button
    lateinit var progressBar: ProgressBar
    lateinit var loginBtnTextView: TextView

    //Firebase
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        confirmEditText = findViewById(R.id.confirm_password_edit_text)
        createAccountButton = findViewById(R.id.create_account_btn)
        progressBar = findViewById(R.id.progress_bar)
        loginBtnTextView = findViewById(R.id.login_text_view_button)


        createAccountButton.setOnClickListener {v ->
            createAccount()
        }
        loginBtnTextView.setOnClickListener { v ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount() {
        val email =  emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmEditText.text.toString()

        val isValidated = validation(email, password, confirmPassword)

        if(!isValidated){
            return
        }

        createAccountInFirebase(email, password)
    }

    private fun createAccountInFirebase(email: String, password: String) {
        changeInProgress(true)

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            this.makeToast("Complete")
            changeInProgress(false)
        }
            .addOnCanceledListener {
                this.makeToast("Failed")
            }.addOnSuccessListener {
                //Create Account is Done
                this.makeToast(it.user?.email.toString())
                auth.currentUser?.sendEmailVerification()
                auth.signOut()
                finish()
            }
    }

    private fun changeInProgress(inProgress: Boolean){
        if(inProgress){
            progressBar.visibility = View.VISIBLE
            createAccountButton.visibility = View.GONE
        }else{
            progressBar.visibility = View.GONE
            createAccountButton.visibility = View.VISIBLE
        }
    }


    private fun validation(email: String, password: String, confirmPassword: String ): Boolean {
        // Validation the data

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.error = "Email is invalid"
            return false
        }
        if(password.length < 6){
            passwordEditText.error = "Password length is invalid"
            return false
        }
        if(password != confirmPassword){
            confirmEditText.error = "Password not matches"
            return false
        }
        return true
    }
}

fun Context.makeToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}



