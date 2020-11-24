package uz.xdevelop.exammonth3.presentation.mvp.presenters

import uz.xdevelop.exammonth3.data.models.network_models.VerifyData
import uz.xdevelop.exammonth3.presentation.mvp.contracts.VerifyContract

class VerifyPresenter(
    private val view: VerifyContract.View,
    private val model: VerifyContract.Model
) : VerifyContract.Presenter {

    override fun clickVerify() {
//        view.openMain()
        val data = VerifyData("+998" + view.getPhone(), view.getCode())

        if (data.code.length != 6) {
            view.showMessage("Code's length should be 6")
            return
        }
        view.showProgressDialog()
        model.verify(data) { res ->
//            res.onResource { view.showMessage() }
            res.onMessage { view.showMessage(res.getMessageOrNull()) }
            res.onFailure { view.showErrorMessage("There was something wrong") }
            res.onData { view.openMain() }
            view.hideProgressDialog()
        }
    }

}