package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import uz.xdevelop.exammonth3.data.models.network_models.LoginData
import uz.xdevelop.exammonth3.data.repositories.LoginRepository
import uz.xdevelop.exammonth3.data.source.remote.api.AuthApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.presentation.mvp.contracts.LoginContract
import uz.xdevelop.exammonth3.presentation.mvp.presenters.LoginPresenter
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.databinding.FragmentLoginBinding
import uz.xdevelop.exammonth3.utils.navigate

class LoginFragment : BaseFragment<FragmentLoginBinding>(), LoginContract.View {
    //    private val api = ApiClient.retrofit.create(LoginApi::class.java)
    private val apiSimple = ApiClient.retrofitSimple.create(AuthApi::class.java)
    private val repository: LoginContract.Model by lazy { LoginRepository(apiSimple) }
    private lateinit var presenter: LoginContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViews()
    }

    private fun loadViews() {
        binding.apply {
            buttonLogin.setOnClickListener { presenter.clickLogin() }
            btnForgot.setOnClickListener { presenter.clickForgot() }
            btnRegister.setOnClickListener { presenter.clickRegister() }

            textPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        layoutPassword.error = null
                        layoutPassword.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            textPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        layoutPhone.error = null
                        layoutPhone.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
        presenter = LoginPresenter(this, repository)


    }

    override fun getPassword(): String = binding.textPassword.text.toString()
    override fun getPhone(): String = binding.textPhone.unmaskedText.toString()
    override fun getRemember(): Boolean = binding.btnRemember.isChecked

    override fun clear() {
        binding.apply {
            textPassword.text?.clear()
            textPhone.text?.clear()
        }
    }

    override fun openRegister() {
        navigate(RegistrationFragment(), addToBackStack = true)
    }

    override fun openForgot() {
        navigate(ForgotFragment(), addToBackStack = true)
    }

    override fun openMain(boolean: Boolean) {
        navigate(CardFragment(), addToBackStack = boolean)
        clear()
    }

    override fun showProgressDialog() = showProgress()

    override fun hideProgressDialog() = hideProgress()

    override fun showMessage(string: String?) {
//        message(string)
        when {
            string!!.contains("Phone's") -> {
                binding.layoutPhone.error = string
                binding.textPhone.requestFocus()
            }
            string.contains("Password's") -> {
                binding.layoutPassword.error = string
                binding.textPassword.requestFocus()
            }
            else -> {
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Info!")
                    .setMessage(string)
                    .setPositiveButton("OK") { _, _ -> }
                    .create()
                dialog.show()
            }
        }
    }

    override fun showErrorMessage(string: String?) {
//        message(string)
        val dialog = AlertDialog.Builder(context)
            .setTitle("Error!")
            .setIcon(R.drawable.ic_baseline_error_24)
            .setMessage(string)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        dialog.show()
    }

    override fun openVerify(data: LoginData) {
        val fr = VerifyFragment()
        fr.pNumber = (data.phoneNumber.removePrefix("+998"))
        navigate(fr, addToBackStack = true)

    }

    override fun getLayoutId(): Int = R.layout.fragment_login
}