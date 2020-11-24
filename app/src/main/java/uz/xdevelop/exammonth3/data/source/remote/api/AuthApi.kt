package uz.xdevelop.exammonth3.data.source.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import uz.xdevelop.exammonth3.data.models.network_models.*

interface AuthApi {

    @POST("contact/login")
    fun login(
        @Body contactData: LoginData
    ): Call<ResponseData<String>>

    @POST("contact/register")
    fun register(
        @Body contactData: RegisterUserData
    ): Call<ResponseData<String>>

    @POST("contact/verify")
    fun verify(
        @Body verifyData: VerifyData
    ): Call<ResponseData<String>>

    @POST("contact/reset")
    fun sendPhone(
        @Body resetData: ResetData
    ): Call<ResponseData<String>>

    @POST("contact/password")
    fun restore(
        @Body forgotData: ForgotData
    ): Call<ResponseData<Any>>
}