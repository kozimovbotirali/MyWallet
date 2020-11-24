package uz.xdevelop.exammonth3.data.source.remote.api

import retrofit2.Call
import retrofit2.http.*
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData

interface ContactApi {
    /**
     * 1. Get all contacts
     * */
    @GET("contact")
    fun getAll(): Call<ResponseData<List<ContactData>>>

    /**
     * 2. add new a contact
     * */
    @POST("contact")
    fun add(@Body contactData: ContactData): Call<ResponseData<ContactData>>

    /**
     * 3. remove a contact
     * */
    @HTTP(method = "DELETE", path = "contact", hasBody = true)
    fun remove(@Body contactData: ContactData): Call<ResponseData<ContactData>>

    /**
     * 3. update a contact
     * */
    @PUT("contact")
    fun update(@Body contactData: ContactData): Call<ResponseData<ContactData>>
}