package AccountHelper

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kawunus.glossario.R
import com.kawunus.glossario.RegisterActivity

class AccountHelper(private val act: RegisterActivity) {
    private lateinit var signInClient: GoogleSignInClient

    fun signUpWithEmail(
        email: String,
        password: String,
        birthdayDate: String
    ) {
        if (email.isNotEmpty() && password.isNotEmpty() && birthdayDate.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    } else {
                        showErrorToast(act.getString(R.string.sign_up_error_firebase))
                    }
                }
        } else {
            showErrorToast(act.getString(R.string.sign_up_error_empty))
        }
    }

    fun signInWithEmail(
        email: String,
        password: String
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    } else {
                        showErrorToast(act.getString(R.string.sign_in_error_firebase))
                    }
                }
        } else {
            showErrorToast(act.getString(R.string.sign_in_error_empty))
        }
    }

    fun resetPassword(email: String) {
        if (email.isNotEmpty()) {
            act.mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener{
                    task->
                    if (task.isSuccessful)
                    {
                        
                    }
                    else {
                        showErrorToast(act.getString(R.string.reset_error_firebase))
                    }
                }
        } else {
            showErrorToast(act.getString(R.string.reset_empty))
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(act, message, Toast.LENGTH_LONG)
            .show()
    }
}