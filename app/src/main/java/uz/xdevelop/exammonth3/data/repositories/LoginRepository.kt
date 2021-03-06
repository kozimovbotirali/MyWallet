package uz.xdevelop.exammonth3.data.repositories

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xdevelop.exammonth3.data.models.network_models.LoginData
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.source.local.storage.LocalStorage
import uz.xdevelop.exammonth3.data.source.remote.api.AuthApi
import uz.xdevelop.exammonth3.presentation.mvp.contracts.LoginContract
import uz.xdevelop.exammonth3.utils.ThreadsHelper
import uz.xdevelop.exammonth3.utils.network.ResultData

class LoginRepository(private val api: AuthApi) : LoginContract.Model, ThreadsHelper() {
    private var storage = LocalStorage.instance

    override fun login(loginData: LoginData, block: (ResultData<String>) -> Unit) {
        api.login(loginData).enqueue(object : Callback<ResponseData<String>> {
            override fun onFailure(call: Call<ResponseData<String>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(
                call: Call<ResponseData<String>>,
                response: Response<ResponseData<String>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> {
                        block(ResultData.message(res.message))
                    }
                    res.status == "OK" -> {
                        val token = res.data
                        storage.token = token ?: ""
                        block(ResultData.data(token ?: ""))
                    }
                }
            }
        })
    }

    override fun remember(loginData: LoginData) {
        storage.remember = loginData.password != ""
    }

    override fun rememberType(): Boolean = storage.remember
}