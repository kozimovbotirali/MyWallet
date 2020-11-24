package uz.xdevelop.exammonth3.data.repositories

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xdevelop.exammonth3.data.source.remote.api.ContactApi
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.source.local.room.AppDatabase
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData
import uz.xdevelop.exammonth3.data.source.local.storage.LocalStorage
import uz.xdevelop.exammonth3.presentation.mvp.contracts.ContactContract
import uz.xdevelop.exammonth3.utils.network.ResultData

class ContactRepository(private val contactApi: ContactApi) : ContactContract.Model {
    private val dao = AppDatabase.getDatabase().contactsDao()

    override fun insertToRoom(data: ContactData?): Long = dao.insert(data!!)

    override fun insertAllToRoom(data: List<ContactData>): List<Long> = dao.insertAll(data)

    override fun deleteAllFromRoom() {
        dao.deleteAll()
    }

    override fun updateInRoom(data: ContactData?): Int = dao.update(data!!)

    override fun deleteFromRoom(data: ContactData?): Int = dao.delete(data!!)

    override fun getAllFromRoom(): List<ContactData> = dao.getAll()
    override fun logOut() {
        LocalStorage.instance.remember = false
        dao.deleteAll()
    }

    override fun getAllContactsFromServer(block: (ResultData<List<ContactData>?>) -> Unit) {
        contactApi.getAll().enqueue(object : Callback<ResponseData<List<ContactData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<ContactData>>>,
                response: Response<ResponseData<List<ContactData>>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> {
                        block(ResultData.message(res.message))
                    }
                    res.status == "OK" -> {
                        block(ResultData.data(res.data))
                    }
                }
                if (response.code() >= 500) {
                    block(ResultData.failure(Throwable("Returns empty body. Please connect with call center!")))
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ContactData>>>, t: Throwable) {
                block(ResultData.failure(t))
            }

        })
    }

    override fun insertToServer(contact: ContactData, block: (ResultData<ContactData?>) -> Unit) {
        contactApi.add(contact).enqueue(object : Callback<ResponseData<ContactData>> {
            override fun onResponse(
                call: Call<ResponseData<ContactData>>,
                response: Response<ResponseData<ContactData>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> {
                        block(ResultData.message(res.message))
                    }
                    res.status == "OK" -> {
                        block(ResultData.data(res.data))
                    }
                }
                if (response.code() >= 500) {
                    block(ResultData.failure(Throwable("Returns empty body. Please connect with call center!")))
                }
            }

            override fun onFailure(call: Call<ResponseData<ContactData>>, t: Throwable) {
                block(ResultData.failure(t))
            }
        })
    }

    override fun updateInServer(contact: ContactData, block: (ResultData<ContactData?>) -> Unit) {
        contactApi.update(contact).enqueue(object : Callback<ResponseData<ContactData>> {
            override fun onResponse(
                call: Call<ResponseData<ContactData>>,
                response: Response<ResponseData<ContactData>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> {
                        block(ResultData.message(res.message))
                    }
                    res.status == "OK" -> {
                        block(ResultData.data(res.data))
                    }
                }
                if (response.code() >= 500) {
                    block(ResultData.failure(Throwable("Returns empty body. Please connect with call center!")))
                }
            }

            override fun onFailure(call: Call<ResponseData<ContactData>>, t: Throwable) {
                block(ResultData.failure(t))
            }
        })
    }

    override fun deleteFromServer(contact: ContactData, block: (ResultData<ContactData?>) -> Unit) {
        contactApi.remove(contact).enqueue(object : Callback<ResponseData<ContactData>> {
            override fun onResponse(
                call: Call<ResponseData<ContactData>>,
                response: Response<ResponseData<ContactData>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> {
                        block(ResultData.message(res.message))
                    }
                    res.status == "OK" -> {
                        block(ResultData.data(res.data))
                    }
                }
                if (response.code() >= 500) {
                    block(ResultData.failure(Throwable("Returns empty body. Please connect with call center!")))
                }
            }

            override fun onFailure(call: Call<ResponseData<ContactData>>, t: Throwable) {
                block(ResultData.failure(t))
            }
        })
    }
}