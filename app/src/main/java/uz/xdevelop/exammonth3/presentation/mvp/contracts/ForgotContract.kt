package uz.xdevelop.exammonth3.presentation.mvp.contracts

import uz.xdevelop.exammonth3.data.models.network_models.ForgotData
import uz.xdevelop.exammonth3.data.models.network_models.ResetData
import uz.xdevelop.exammonth3.utils.network.ResultData

interface ForgotContract {
    interface Model {
        fun sendPhone(resetData: ResetData, block: (ResultData<String>) -> Unit)
        fun restorePassword(forgotData: ForgotData, block: (ResultData<Any>) -> Unit)
    }

    interface View {
        fun getCode(): String
        fun getPhone(): String
        fun getPassword(): String
        fun clear()
        fun toBack()
        fun prepareRestore()
        fun showProgressDialog()
        fun hideProgressDialog()
        fun showMessage(string: String?)
        fun showErrorMessage(string: String?)
    }

    interface Presenter {
        fun clickRestore()
    }
}