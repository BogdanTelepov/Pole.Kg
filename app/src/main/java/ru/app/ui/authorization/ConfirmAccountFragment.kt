package ru.app.ui.authorization

import android.content.Context.MODE_PRIVATE
import android.graphics.Color.red
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.Navigation
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_confirm_account.*
import kotlinx.android.synthetic.main.fragment_input_phone_number.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.support.v4.act
import ru.app.R
import ru.app.databinding.FragmentConfirmAccountBinding
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class ConfirmAccountFragment : Fragment() {
    companion object {
        private const val TAG = "InputPhone"
    }
    lateinit var verificationId: String
    lateinit var phoneNumber: String
    lateinit var auth: FirebaseAuth
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var type: Int = 0
    private var _binding: FragmentConfirmAccountBinding? = null
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmAccountBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle = this.arguments
        auth = FirebaseAuth.getInstance()
        phoneNumber = bundle?.getString("phone_number").toString()

        Log.e("phone" , phoneNumber)

        type = bundle?.getInt("type", -1)!!

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.e(TAG, "onVerificationCompleted:$credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e(TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Log.e("error", e.message.toString())
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

        binding?.btnConfirmCode?.setOnClickListener {
            Log.e("confirmcode", "is clicked")
            checkEnteredData(it)

        }
        binding?.btnSendCodeAgain?.setOnClickListener {
            checkDataAgain(it)

        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        Log.e("check", "startPhoneVerification")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())    // Activity (for callback binding)
            .setCallbacks(callbacks)             // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(resendToken) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }



    private fun checkDataAgain(it: View) {
            tv_invalid_code.visibility = View.GONE
           resendVerificationCode(phoneNumber, resendToken)
        }


    private fun checkEnteredData(view : View) {
        if (isEmpty(edit_inputCode)){
            edit_inputCode.error = "Поле должно быть заполнено"
            edit_inputCode.setBackgroundResource(R.drawable.background_stroke_red)
        }
        else{
            Log.e("check", "input")
            tv_invalid_code.visibility = View.GONE
            verifyPhoneNumberWithCode(verificationId, edit_inputCode.text.toString())

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
                var user = task.result?.user
                Log.e("firebase user ", user?.uid.toString())
//
                val bundle = Bundle()
                bundle.putString("phoneNumber", phoneNumber)
                bundle.putInt("type", type)
                view?.let { Navigation.findNavController(it).navigate(R.id.action_confirmAccountFragment_to_enterUserNameFragment,bundle ) }
            } else {
                Log.e(TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    tv_invalid_code.visibility = View.VISIBLE
                   // binding?.editInputCode?.error = "Invalid code."
                }
            }

        }
    }
    //if input fields are empty
    private fun isEmpty(text: EditText): Boolean {
        val str: CharSequence = text.text.toString().trim()
        return TextUtils.isEmpty(str)
    }

}