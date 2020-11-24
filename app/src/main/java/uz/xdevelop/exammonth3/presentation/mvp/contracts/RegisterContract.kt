package uz.xdevelop.exammonth3.presentation.mvp.contracts

import uz.xdevelop.exammonth3.data.models.network_models.RegisterUserData
import uz.xdevelop.exammonth3.utils.network.ResultData

interface RegisterContract {
    interface Model {
        fun register(registerUserData: RegisterUserData, block: (ResultData<String>) -> Unit)
    }

    interface View {
        fun getPassword1(): String
        fun getPassword2(): String
        fun getPhone(): String
        fun getName(): String
        fun getSurname(): String
        fun clear()
        fun openLogin()
        fun openVerify()
        fun showProgressDialog()
        fun hideProgressDialog()
        fun showMessage(string: String?)
        fun showErrorMessage(string: String?)
    }

    interface Presenter {
        fun clickLogin()
        fun clickRegister()
    }
}