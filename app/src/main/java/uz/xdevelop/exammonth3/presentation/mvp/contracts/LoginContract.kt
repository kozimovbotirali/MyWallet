package uz.xdevelop.exammonth3.presentation.mvp.contracts

import uz.xdevelop.exammonth3.data.models.network_models.LoginData
import uz.xdevelop.exammonth3.utils.network.ResultData

interface LoginContract {
    interface Model {
        fun login(loginData: LoginData, block: (ResultData<String>) -> Unit)
        fun remember(loginData: LoginData)
        fun rememberType(): Boolean
    }

    interface View {
        fun getPassword(): String
        fun getPhone(): String
        fun getRemember(): Boolean
        fun clear()
        fun openRegister()
        fun openForgot()
        fun openMain(boolean: Boolean)
        fun showProgressDialog()
        fun hideProgressDialog()
        fun showMessage(string: String?)
        fun showErrorMessage(string: String?)
        fun openVerify(data: LoginData)
    }

    interface Presenter {
        fun clickLogin()
        fun clickForgot()
        fun clickRegister()
    }
}