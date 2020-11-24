package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.data.models.network_models.CardData
import uz.xdevelop.exammonth3.data.models.network_models.ResponseData
import uz.xdevelop.exammonth3.data.source.remote.api.CardApi
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.databinding.FragmentCardBinding
import uz.xdevelop.exammonth3.presentation.ui.adapters.CardAdapter
import uz.xdevelop.exammonth3.presentation.ui.adapters.HorizontalLayout
import uz.xdevelop.exammonth3.utils.navigate
import uz.xdevelop.exammonth3.utils.network.ResultData
import java.util.concurrent.Executors

class CardFragment : BaseFragment<FragmentCardBinding>() {
    private lateinit var adapter: CardAdapter
    private val cardApi = ApiClient.retrofit.create(CardApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private val handle = Handler(Looper.getMainLooper())

    override fun getLayoutId(): Int = R.layout.fragment_card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        /*setFragmentResultListener("addCard") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.get("card")
            result?.apply {
                val data = this as CardData
                val ls = adapter.currentList.toMutableList()
                ls.add(data)
                adapter.submitList(ls)
            }
            // Do something with the result...
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initializeRecyclerView()

        loadData()
    }

    private fun loadData() {
        showProgressDialog()
        getAllCardsFromServer {
            runOnWorkerThread {
                it.onData {
//                    model.deleteAllFromRoom()
//                    model.insertAllToRoom(it!!)
                    runOnUiThread {
                        it?.let { it1 -> initData(it1) }
                    }
                }
            }

            runOnUiThread {
                it.onFailure {
                    onFailure(it, 1)
                }
                it.onMessage { showMessage(it) }
                hideProgressDialog()
            }
        }
    }

    private fun initializeRecyclerView() {
        (activity as AppCompatActivity).title = "My Wallet"
        adapter = CardAdapter()

        binding.apply {
            list.adapter = adapter
            list.layoutManager =
                HorizontalLayout(context)

            btnAddCard.setOnClickListener {
                openAddCardFragment()
            }

            val ls = adapter.currentList.toMutableList()
            adapter.submitList(ls)
        }
    }

    private fun openAddCardFragment() {
        navigate(
            AddCardFragment(),
            addToBackStack = true
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_card -> {
                openAddCardFragment()
            }
            R.id.menu_log_out -> {
                backToLogin()
            }
        }
        return true
    }

    private fun initData(data: List<CardData>) {
        adapter.submitList(data)
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

    /*fun insert(data: CardData) {
        val ls = adapter.currentList.toMutableList()
        ls.add(data)
        adapter.submitList(ls)
        binding.list.smoothScrollToPosition(ls.size - 1)
    }

    fun update(data: CardData) {
        val ls = adapter.currentList.toMutableList()
        val pos = ls.indexOfFirst { data.id == it.id }
        ls[pos] = data
        adapter.submitList(ls)
        adapter.notifyItemChanged(pos)
    }*/

    fun delete(data: CardData) {
        val ls = adapter.currentList.toMutableList()
        val pos = ls.indexOfFirst { data.id == it.id }
        ls.removeAt(pos)
        adapter.submitList(ls)
    }

    fun backToLogin() {
        navigate(
            LoginFragment(),
            popBackStack = true,
            popUpToInclusive = true,
            popUpTo = LoginFragment::class.java.simpleName
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

    fun onFailure(t: Throwable, type: Int = 0) {
        onResponseFailure(t)
        hideProgressDialog()
        /*if (type == 1) {
            loadLocal()
        }*/
    }

    private fun getAllCardsFromServer(block: (ResultData<List<CardData>?>) -> Unit) {
        cardApi.getAll().enqueue(object : Callback<ResponseData<List<CardData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<CardData>>>,
                response: Response<ResponseData<List<CardData>>>
            ) {
                val res = response.body()
                when {
//                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res == null -> block(ResultData.message("Returns empty body. Please connect with call center!"))
                    res.status == "ERROR" -> {
                        block(ResultData.message(res.message))
                    }
                    res.status == "OK" -> {
                        block(ResultData.data(res.data))
                    }
                }
                if (response.code() >= 500) {
                    block(ResultData.failure(Throwable("Returns empty body. Please connect with call center!")))
                }
            }

            override fun onFailure(call: Call<ResponseData<List<CardData>>>, t: Throwable) {
                block(ResultData.failure(t))
            }

        })
    }
}