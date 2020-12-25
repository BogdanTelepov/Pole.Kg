package ru.app.ui.authorization

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.se.omapi.Session
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.book_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_authorization_confirm_account.*
import kotlinx.android.synthetic.main.fragment_authorization_confirm_account.view.*
import kotlinx.android.synthetic.main.fragment_authorization_confirm_account.view.btn_confirmCode
import kotlinx.android.synthetic.main.fragment_authorization_confirm_account.view.btn_sendCodeAgain
import kotlinx.android.synthetic.main.fragment_authorization_confirm_account.view.tv_invalid_code
import kotlinx.android.synthetic.main.fragment_confirm_account.*
import kotlinx.android.synthetic.main.fragment_confirm_account.tv_invalid_code
import kotlinx.android.synthetic.main.fragment_confirm_account.view.*
import retrofit2.Call
import retrofit2.Response
import ru.app.R
import ru.app.databinding.FragmentConfirmAccountBinding
import ru.app.models.auth.LoginRequest
import ru.app.models.auth.LoginResponse
import ru.app.network.ApiService
import ru.app.network.RetrofitInstance
import ru.app.network.SessionManager
import java.util.concurrent.TimeUnit

class AuthorizationConfirmAccountFragment: Fragment(){

    companion object {
        private const val TAG = "AuthorizationConfirm"
    }
    lateinit var verificationId: String
    lateinit var phoneNumber: String
    lateinit var auth: FirebaseAuth
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var sessionManager: SessionManager
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var progressBar : ProgressBar
    lateinit var rootView : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =
                inflater.inflate(R.layout.fragment_authorization_confirm_account, container, false)
        return  rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val data = this.arguments
        phoneNumber = data?.getString("phoneNumber").toString()
        auth = FirebaseAuth.getInstance()
        progressBar = rootView.authorization_progress_bar
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.e(TAG, "onVerificationCompleted:$credential")
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Log.e(TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Log.e("error", e.message.toString())
                    view.tv_invalid_code.text = getString(R.string.invalid_phone_number)
                    view.tv_invalid_code.visibility = View.VISIBLE
                    view.btn_confirmCode.isEnabled = false
                }
            }
            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                verificationId = id
                resendToken = token
            }

            override fun onCodeAutoRetrievalTimeOut(string: String) {
                super.onCodeAutoRetrievalTimeOut(string)
                Log.e("TimeOut", string)
            }
        }

        startPhoneNumberVerification(phoneNumber)

        rootView.btn_confirmCode.setOnClickListener {
            checkEnteredData(rootView)

        }
        rootView.btn_sendCodeAgain.setOnClickListener {
            checkDataAgain(rootView)
        }
    }


    private fun login(phoneNumber: String) {
        val retrofit =  RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val loginRequest = LoginRequest(phoneNumber)
        val call = retrofit.login(loginRequest)
        call.enqueue(object: retrofit2.Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "Проверьте интернет соединение", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    Log.e("AUTHORIZATION", "${response.body()?.token}")
                    response.body()?.token?.let { sessionManager.saveAuthToken(it) }
                    response.body()?.type?.let { response.body()?.phone_number?.let { it1 ->
                        sessionManager.saveUserType(it,
                            it1
                        )
                    } }
                    progressBar.visibility = View.GONE
                    view?.let { Navigation.findNavController(it).navigate(R.id.action_authorizationConfirmAccountFragment_to_profileNotRegisteredUsers) }
                    showDialog()
                }
                else{
                    Toast.makeText(context, "Такой пользователь не зарегистрирован", Toast.LENGTH_LONG).show()
                }
                }
        })
    }

    private fun showDialog() {
            val alertDialog = LayoutInflater.from(context).inflate(R.layout.authorization_alert, null)
            val builder = AlertDialog.Builder(context)
                    .setView(alertDialog)
                    .show()
            alertDialog.book_alert_btn_ok.setOnClickListener {
                builder.dismiss()
            }
        }


    private fun startPhoneNumberVerification(phoneNumber: String) {
        Log.e("check", "startPhoneVerification")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
        if (token != null) {
            optionsBuilder.setForceResendingToken(resendToken)
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun checkDataAgain(view: View) {
        view.tv_invalid_code.visibility = View.GONE
        resendVerificationCode(phoneNumber, resendToken)
    }


    private fun checkEnteredData(view : View) {
        if (isEmpty(view.auth_edit_inputCode)){
            view.auth_edit_inputCode.error = getString(R.string.field_should_be_filled)
            view.auth_edit_inputCode.setBackgroundResource(R.drawable.background_stroke_red)
        }
        else{
            Log.e("check", "input")
            view.tv_invalid_code.visibility = View.GONE
            verifyPhoneNumberWithCode(verificationId, view.auth_edit_inputCode.text.toString())

        }
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e(TAG, "signInWithCredential:success")
                login(phoneNumber)
                progressBar.visibility = View.VISIBLE
             }
             else {
                Log.e(TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    rootView.auth_edit_inputCode.setBackgroundResource(R.drawable.background_stroke_red)
                    rootView.tv_invalid_code.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun isEmpty(text: EditText): Boolean {
        val str: CharSequence = text.text.toString().trim()
        return TextUtils.isEmpty(str)
    }

}