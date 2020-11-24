package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import uz.xdevelop.exammonth3.data.repositories.LoginRepository
import uz.xdevelop.exammonth3.data.source.local.storage.LocalStorage
import uz.xdevelop.exammonth3.data.source.remote.api.AuthApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.presentation.mvp.contracts.LoginContract
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.databinding.FragmentSplashBinding
import uz.xdevelop.exammonth3.utils.navigate
import java.util.concurrent.Executors

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    private val executor = Executors.newSingleThreadExecutor()
    private val handle = Handler(Looper.getMainLooper())
    private val apiSimple = ApiClient.retrofitSimple.create(AuthApi::class.java)
    private var storage = LocalStorage.instance
    private val model: LoginContract.Model by lazy { LoginRepository(apiSimple) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runOnWorkerThread {
            Thread.sleep(2000)
            if (storage.isFirst) {
                runOnUiThread {
                    navigate(IntoFragmentMain())
                }
            } else {
                if (model.rememberType()) {
                    openMain()
                } else {
                    openLogin()
                }
            }

        }
    }

    private fun openLogin() {
        runOnUiThread {
            navigate(LoginFragment())
        }
    }

    private fun openMain() {
        runOnUiThread {
            navigate(CardFragment())
        }
    }


    private fun runOnUiThread(f: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            f()
        } else {
            handle.post { f() }
        }
    }

    private fun runOnWorkerThread(f: () -> Unit) {
        executor.execute(f)
    }

    override fun getLayoutId(): Int = R.layout.fragment_splash
}