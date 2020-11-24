package uz.xdevelop.exammonth3.data.source.remote.api

import retrofit2.Call
import retrofit2.http.*
import uz.xdevelop.exammonth3.data.models.network_models.CardData
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.models.network_models.VerifyCard
import uz.xdevelop.exammonth3.data.models.network_models.VerifyData
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData

interface CardApi {
    /**
     * 1. Get all cards
     * */
    @GET("card")
    fun getAll(): Call<ResponseData<List<CardData>>>

    /**
     * 2. add new a card
     * */
    @POST("card")
    fun add(@Body cardData: CardData): Call<ResponseData<Any>>

    /**
     * 3. verify phone number
     * */
    @POST("card/verify")
    fun verifyCardNumber(
        @Body verifyCard: VerifyCard
    ): Call<ResponseData<CardData>>
}