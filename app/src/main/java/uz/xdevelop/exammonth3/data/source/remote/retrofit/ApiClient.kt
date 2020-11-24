package uz.xdevelop.exammonth3.data.source.remote.retrofit

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.xdevelop.exammonth3.app.MyApp
import uz.xdevelop.exammonth3.data.source.local.storage.LocalStorage

object ApiClient {
    private val logging =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(ChuckInterceptor(MyApp.instance))//for seeing responses and requests from emulator
        .addInterceptor {
            val requestOld = it.request()
            val request = requestOld.newBuilder()
                .removeHeader("token")//additional
                .addHeader("token", LocalStorage.instance.token)
                .method(requestOld.method, requestOld.body)
                .build()
            val response = it.proceed(request)
            response
        }
        .build()

    private val clientSimple = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(ChuckInterceptor(MyApp.instance))//for seeing responses and requests from emulator
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://161.35.73.101:99/")
//        .baseUrl("https://9839ad53897b.ngrok.io/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitSimple: Retrofit = Retrofit.Builder()
        .baseUrl("http://161.35.73.101:99/")
//        .baseUrl("https://9839ad53897b.ngrok.io/")
        .client(clientSimple)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}