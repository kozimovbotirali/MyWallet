package uz.xdevelop.exammonth3.data.repositories

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xdevelop.exammonth3.data.models.network_models.ForgotData
import uz.xdevelop.exammonth3.data.models.network_models.ResetData
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.source.local.storage.LocalStorage
import uz.xdevelop.exammonth3.data.source.remote.api.AuthApi
import uz.xdevelop.exammonth3.presentation.mvp.contracts.ForgotContract
import uz.xdevelop.exammonth3.utils.ThreadsHelper
import uz.xdevelop.exammonth3.utils.network.ResultData

class ForgotRepository(private val api: AuthApi) : ForgotContract.Model, ThreadsHelper() {
    private var storage = LocalStorage.instance

    override fun sendPhone(resetData: ResetData, block: (ResultData<String>) -> Unit) {
        api.sendPhone(resetData).enqueue(object : Callback<ResponseData<String>> {
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
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> {
                        block(ResultData.data(res.message))
                    }
                }
            }
        })
    }

    override fun restorePassword(forgotData: ForgotData, block: (ResultData<Any>) -> Unit) {
        api.restore(forgotData).enqueue(object : Callback<ResponseData<Any>> {
            override fun onFailure(call: Call<ResponseData<Any>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> {
                        block(ResultData.data(res.message))
                    }
                }
            }
        })
    }
}