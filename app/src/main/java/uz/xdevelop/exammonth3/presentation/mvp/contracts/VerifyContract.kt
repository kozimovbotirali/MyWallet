package uz.xdevelop.exammonth3.presentation.mvp.contracts

import uz.xdevelop.exammonth3.data.models.network_models.VerifyData
import uz.xdevelop.exammonth3.utils.network.ResultData

interface VerifyContract {
    interface Model {
        fun verify(verifyData: VerifyData, block: (ResultData<String>) -> Unit)
    }

    interface View {
        fun getCode(): String
        fun getPhone(): String
        fun clear()
        fun openMain()
        fun showProgressDialog()
        fun hideProgressDialog()
        fun showMessage(string: String?)
        fun showErrorMessage(string: String?)
    }

    interface Presenter {
        fun clickVerify()
    }
}