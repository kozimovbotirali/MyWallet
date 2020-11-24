package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.card_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.data.models.network_models.CardData
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.source.remote.api.CardApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.databinding.FragmentAddCardBinding
import uz.xdevelop.exammonth3.utils.navigate
import uz.xdevelop.exammonth3.utils.network.ResultData
import uz.xdevelop.exammonth3.utils.putArguments
import uz.xdevelop.exammonth3.utils.setReadOnly
import java.util.concurrent.Executors

class AddCardFragment : BaseFragment<FragmentAddCardBinding>() {
    private val cardApi = ApiClient.retrofit.create(CardApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private val handle = Handler(Looper.getMainLooper())
    private var color = 4

    override fun getLayoutId(): Int = R.layout.fragment_add_card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadViews()
    }

    private fun loadViews() {
        (activity as AppCompatActivity).title = "Add Card"
        binding.apply {
            text_pan.setReadOnly(true)
            text_expired_at.setReadOnly(true)
            buttonSave.setOnClickListener {
                val data = CardData(
                    pan = inputPan.unmaskedText,
                    expiredAt = inputExpired.text.toString(),
                    name = inputName.text.toString(),
                    color = color
                )
                Log.d("TTTT", data.toString())
                if (data.pan?.length != 16) {
                    layoutPan.error = "Please fill!"
                    return@setOnClickListener
                }

                if (data.expiredAt?.length != 5) {
                    layoutExpired.error = "Please fill!"
                    return@setOnClickListener
                }

                if (data.name?.isEmpty()!!) {
                    layoutName.error = "Please fill!"
                    return@setOnClickListener
                }

                showProgressDialog()
                addCard(data) {
                    it.onMessage {
                        showMessage(it)
                    }
                    it.onFailure { onResponseFailure(it) }
                    it.onData {
                        val dialog = AlertDialog.Builder(context)
                            .setTitle("Info!")
                            .setMessage(it)
                            .setPositiveButton("OK") { _, _ ->
                                openVerify(data.pan)
                            }
                            .create()
                        dialog.show()
                    }
                    hideProgressDialog()
                }
            }

            radioGroup.setOnCheckedChangeListener { _, i ->
                val bg = when (i) {
                    R.id.radio1 -> {
                        color = 0
                        R.drawable.color1
                    }
                    R.id.radio2 -> {
                        color = 1
                        R.drawable.color2
                    }
                    R.id.radio3 -> {
                        color = 2
                        R.drawable.color3
                    }
                    R.id.radio4 -> {
                        color = 3
                        R.drawable.color4
                    }
                    R.id.radio5 -> {
                        color = 4
                        R.drawable.color5
                    }
                    else -> {
                        color = 5
                        R.drawable.color6
                    }
                }
                cardBg.setImageResource(bg)
            }


            inputPan.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (s.toString().isNotEmpty()) {
                        layoutPan.error = null
                        layoutPan.isErrorEnabled = false
                    }
                    text_pan.setMaskedText(s.toString().replace(" ", ""))
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            inputExpired.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (s.toString().isNotEmpty()) {
                        layoutExpired.error = null
                        layoutExpired.isErrorEnabled = false
                    }
                    text_expired_at.setMaskedText(s.toString().replace("/", ""))
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            inputName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (s.toString().isNotEmpty()) {
                        layoutName.error = null
                        layoutName.isErrorEnabled = false
                    }
                    text_name.text = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun openVerify(pan: String) {
        navigate(VerifyCardFragment().putArguments { putString("pan", pan) }, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                activity?.onBackPressed()
                backToLogin()
            }
        }
        return true
    }

    private fun onResponseFailure(t: Throwable) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Error!")
            .setIcon(R.drawable.ic_baseline_error_24)
            .setMessage(t.message)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        dialog.show()
    }

    fun showMessage(str: String?) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Info!")
            .setMessage(str)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        dialog.show()
    }

    fun showProgressDialog() = showProgress()

    fun hideProgressDialog() = hideProgress()

    fun backToLogin() {
        navigate(
            CardFragment(),
            popBackStack = true,
            popUpToInclusive = true,
            popUpTo = CardFragment::class.java.simpleName
        )
    }

    private fun runOnWorkerThread(f: () -> Unit) {
        executor.execute(f)
    }

    private fun runOnUiThread(f: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            f()
        } else {
            handle.post { f() }
        }
    }

    private fun addCard(
        card: CardData,
        block: ((ResultData<String>)) -> Unit
    ) {
        cardApi.add(card).enqueue(object : Callback<ResponseData<Any>> {
            override fun onFailure(call: Call<ResponseData<Any>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> {
                        block(ResultData.data(res.message))
                    }
                }
            }
        })
    }
}