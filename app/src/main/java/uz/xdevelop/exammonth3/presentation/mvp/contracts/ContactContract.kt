package uz.xdevelop.exammonth3.presentation.mvp.contracts

import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData
import uz.xdevelop.exammonth3.utils.network.ResultData

interface ContactContract {
    interface Model {
        fun insertToRoom(data: ContactData?): Long
        fun insertAllToRoom(data: List<ContactData>): List<Long>
        fun deleteAllFromRoom()
        fun updateInRoom(data: ContactData?): Int
        fun deleteFromRoom(data: ContactData?): Int
        fun getAllFromRoom(): List<ContactData>
        fun logOut()

        fun getAllContactsFromServer(block: (ResultData<List<ContactData>?>) -> Unit)
        fun insertToServer(contact: ContactData, block: (ResultData<ContactData?>) -> Unit)
        fun updateInServer(contact: ContactData, block: (ResultData<ContactData?>) -> Unit)
        fun deleteFromServer(contact: ContactData, block: (ResultData<ContactData?>) -> Unit)
    }

    interface View {
        fun initData(data: List<ContactData>)
        fun onResponseFailure(t: Throwable)
        fun showMessage(str: String)
        fun showProgressDialog()
        fun hideProgressDialog()
        fun openAddDialog()
        fun openEditDialog(data: ContactData)
        fun openDeleteDialog(data: ContactData)
        fun insert(data: ContactData)
        fun update(data: ContactData)
        fun delete(data: ContactData)
        fun backToLogin()
    }

    interface Presenter {
        fun requestDataFromServer()
        fun createContact(data: ContactData)
        fun editContact(data: ContactData)
        fun deleteContact(data: ContactData)
        fun openEditContactDialog(data: ContactData)
        fun openDeleteContactDialog(data: ContactData)
        fun refreshClicked()
        fun addContactClicked()
        fun logOut()
    }
}