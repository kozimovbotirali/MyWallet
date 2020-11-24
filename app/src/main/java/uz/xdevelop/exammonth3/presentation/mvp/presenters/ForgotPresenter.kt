package uz.xdevelop.exammonth3.presentation.mvp.presenters

import uz.xdevelop.exammonth3.data.models.network_models.ForgotData
import uz.xdevelop.exammonth3.data.models.network_models.ResetData
import uz.xdevelop.exammonth3.presentation.mvp.contracts.ForgotContract

class ForgotPresenter(
    private val view: ForgotContract.View,
    private val model: ForgotContract.Model
) : ForgotContract.Presenter {

    override fun clickRestore() {
//        view.toBack()
        if (view.getCode().isEmpty()) {
            val data = ResetData("+998" + view.getPhone())
            if (data.phoneNumber.length < 13) {
                view.showMessage("Phone's length should be 9")
                return
            }
            view.showProgressDialog()
            model.sendPhone(data) { res ->
                res.onMessage { view.showMessage(res.getMessageOrNull()) }
                res.onFailure { view.showErrorMessage("There was something wrong") }
                res.onData {
                    view.showMessage(it)
                    view.prepareRestore()
                }
                view.hideProgressDialog()
            }

        } else {
            val data = ForgotData("+998" + view.getPhone(), view.getPassword(), view.getCode())
            if (data.code.length != 6) {
                view.showMessage("Code's length should be 6")
                return
            }
            if (data.password.length < 6 || data.password.length > 20) {
                view.showMessage("Password's length should be: min: 6 and max: 20")
                return
            }

            view.showProgressDialog()
            model.restorePassword(data) { res ->
                res.onMessage { view.showMessage(res.getMessageOrNull()) }
                res.onFailure { view.showErrorMessage("There was something wrong") }
                res.onData {
                    view.showMessage(it as String)
                    view.toBack()
                }
                view.hideProgressDialog()
            }
        }
    }

}