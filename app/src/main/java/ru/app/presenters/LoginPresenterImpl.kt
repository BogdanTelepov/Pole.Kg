package ru.app.presenters

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.app.network.RetrofitService
import ru.app.responseModels.LoginResponseModel
import ru.app.view.interfaces.ILoginView

class LoginPresenterImpl(var iLoginView: ILoginView, val context: Context) : ILoginPresenter {

    lateinit var loginResponseModel: LoginResponseModel
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun doLogin(phone: String, password: String) {
        val data = HashMap<String, String>()
        data["phoneNumber"] = phone
        data["password"] = password

        val body = JSONObject(data).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        RetrofitService().getApi().login(body).enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(
                call: Call<LoginResponseModel>,
                response: Response<LoginResponseModel>
            ) {
                if (!response.isSuccessful) {
                    iLoginView.onLoginResult(false, -1)
                } else {
                    loginResponseModel = response.body()!!
                    loginResponseModel.sessionId
                }
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {

            }

        })
    }


    override fun setProgressBarVisibility(visibility: Int) {
        iLoginView.onSetProgressBarVisibility(visibility)
    }
}