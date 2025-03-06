package com.zybooks.workway

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.zybooks.workway.model.User
import kotlinx.coroutines.launch

class AuthManager(private val activity: MainActivity) {
    private var loggingIn = false
    private val auth = Firebase.auth
    private val credentialManager = CredentialManager.create(activity.applicationContext)
    private val activityResultLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data?.getBooleanExtra("LOGOUT", false)
            if (data == true) this.logOut()
        }
    }

    fun run() {
        if (loggingIn) return
        if (loggedIn()) return logIn()
        launchCredentialManager()
    }

    fun loggedIn(): Boolean {
        return auth.currentUser != null
    }

    private fun launchCredentialManager() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(activity.applicationContext.getString(R.string.web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        activity.lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                handleSignIn(result.credential)
            } catch (e: NoCredentialException) {
                Log.e(TAG, "Couldn't find credentials: ${e.localizedMessage}")
                val intent = Intent(Settings.ACTION_ADD_ACCOUNT)
                    .putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                activity.startActivity(intent)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        loggingIn = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    logIn()
                } else {
                    loggingIn = false
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun logIn() {
        val user: User
        if (false /*getUser(auth.currentUser?.uid) != null*/) { // User exists
            // Load user attributes from database
            user = User()
        } else { // User is new
            user = User().apply {
                userID = auth.currentUser?.uid
                name = auth.currentUser?.displayName
                email = auth.currentUser?.email
            }
        }
        val text = "Logged in as ${user.email}"
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        activityResultLauncher.launch(Intent(activity, HomeScreen::class.java))
        loggingIn = false
    }

    private fun logOut() {
        auth.signOut()
        activity.lifecycleScope.launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
            } catch (e: ClearCredentialException) {
                Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
    }

    companion object {
        private const val TAG = "AuthManager"
    }
}
