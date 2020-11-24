package uz.xdevelop.exammonth3.presentation.mvp.presenters

import uz.xdevelop.exammonth3.data.models.network_models.RegisterUserData
import uz.xdevelop.exammonth3.presentation.mvp.contracts.RegisterContract

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val model: RegisterContract.Model
) : RegisterContract.Presenter {

    override fun clickRegister() {
//        view.openVerify()
        val data = RegisterUserData(
            "+998" + view.getPhone(),
            view.getPassword1(),
            view.getSurname(),
            view.getName()
        )
        if (data.firstName == "") {
            view.showMessage("First name's empty!")
            return
        }
        if (data.lastName == "") {
            view.showMessage("Last name's empty!")
            return
        }
        if (view.getPassword1() != view.getPassword2()) {
            view.showMessage("Passwords are different!!")
            return
        }
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
        model.register(data) { res ->
            res.onMessage(view::showErrorMessage)
            res.onFailure { view.showErrorMessage("There was something wrong") }
            res.onData {
                view.showMessage(it)
                view.openVerify()
            }
            view.hideProgressDialog()
        }
    }

    override fun clickLogin() {
        view.openLogin()
    }

}