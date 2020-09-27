package ru.app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.app.R
import ru.app.view.interfaces.ILoginView


class LoginFragment : Fragment(), ILoginView {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onLoginResult(result: Boolean, code: Int) {

    }

    override fun onSetProgressBarVisibility(visibility: Int) {

    }


}








