package uz.xdevelop.exammonth3.presentation.mvp.presenters

import uz.xdevelop.exammonth3.data.models.network_models.LoginData
import uz.xdevelop.exammonth3.presentation.mvp.contracts.LoginContract

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginContract.Model
) : LoginContract.Presenter {

    override fun clickLogin() {
        /*val data = LoginData("+998" + view.getPhone(), view.getPassword())
        view.openVerify(data)*/
        val data = LoginData("+998" + view.getPhone(), view.getPassword())
        if (data.password.matches(Regex("[0-9+]"))) {
            view.showMessage("Phone should not contain only numbers")
            return
        }
        if (data.phoneNumber.length < 13) {
            view.showMessage("Phone's length should be min 9")
            return
        }
        if (data.password.length < 6 || data.password.length > 20) {
            view.showMessage("Password's length should be: min: 6 and max: 20")
            return
        }
        view.showProgressDialog()
        model.login(data) { res ->
            res.onMessage {
                if (it.contains("verify"))
                    view.openVerify(data)
                view.showErrorMessage(res.getMessageOrNull())

            }
            res.onFailure { view.showErrorMessage("There was something wrong") }
            res.onData {
                var b = true
                if (view.getRemember()) {
                    model.remember(data)
                    b = false
                } else {
                    model.remember(LoginData("", ""))
                }
                view.openMain(b)
            }
            view.hideProgressDialog()
        }
    }

    override fun clickForgot() {
        view.openForgot()
    }

    override fun clickRegister() {
        view.openRegister()
    }

}