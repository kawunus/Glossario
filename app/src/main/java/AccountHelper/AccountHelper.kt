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
                        Toast.makeText(
                            act,
                            act.getString(R.string.sign_up_error_firebase),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            Toast.makeText(act, act.getString(R.string.sign_up_error_empty), Toast.LENGTH_LONG)
                .show()
        }
    }
}