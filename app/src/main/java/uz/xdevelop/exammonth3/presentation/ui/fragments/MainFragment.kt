package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import uz.xdevelop.exammonth3.data.source.remote.api.ContactApi
import uz.xdevelop.exammonth3.data.repositories.ContactRepository
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData
import uz.xdevelop.exammonth3.data.source.remote.retrofit.ApiClient
import uz.xdevelop.exammonth3.presentation.mvp.contracts.ContactContract
import uz.xdevelop.exammonth3.presentation.mvp.presenters.ContactPresenter
import uz.xdevelop.exammonth3.presentation.ui.adapters.ContactAdapter
import uz.xdevelop.exammonth3.presentation.ui.dialogs.ContactDialog
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.databinding.FragmentMainBinding
import uz.xdevelop.exammonth3.utils.navigate

class MainFragment : BaseFragment<FragmentMainBinding>(), ContactContract.View {
    private lateinit var presenter: ContactContract.Presenter
    private lateinit var adapter: ContactAdapter
    private val contactApi = ApiClient.retrofit.create(ContactApi::class.java)

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initializeRecyclerView()

        presenter = ContactPresenter(ContactRepository(contactApi), this)
        presenter.requestDataFromServer()
    }

    private fun initializeRecyclerView() {
        (activity as AppCompatActivity).title = "Contacts"
        adapter = ContactAdapter()
        binding.apply {
            list.adapter = adapter
            list.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        adapter.setOnItemDeleteListener {
            presenter.openDeleteContactDialog(it)
        }

        adapter.setOnItemEditListener {
            presenter.openEditContactDialog(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                presenter.addContactClicked()
            }
            R.id.menu_refresh -> {
                presenter.refreshClicked()
            }
            else -> presenter.logOut()
        }
        return true
    }

    override fun initData(data: List<ContactData>) {
        adapter.submitList(data)
    }

    override fun onResponseFailure(t: Throwable) {
        Toast.makeText(
            context,
            "Something went wrong...",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showMessage(str: String) {
        Toast.makeText(
            context,
            "Message: $str",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showProgressDialog() = showProgress()

    override fun hideProgressDialog() = hideProgress()

    override fun openAddDialog() {
        val dialog = ContactDialog(context!!, "Add")
        dialog.setOnClickListener {
            presenter.createContact(it)
        }
        dialog.show()
    }

    override fun openEditDialog(data: ContactData) {
        val dialog = ContactDialog(context!!, "Edit")
        dialog.setContactData(data)
        dialog.setOnClickListener {
            presenter.editContact(it)
        }
        dialog.show()
    }

    override fun openDeleteDialog(data: ContactData) {
        val dialog = AlertDialog.Builder(context!!)
            .setTitle("Confirm")
            .setMessage("Do you want to really delete this contact?")
            .setPositiveButton("Ok") { _, _ ->
                presenter.deleteContact(data)
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    override fun insert(data: ContactData) {
        val ls = adapter.currentList.toMutableList()
        ls.add(data)
        adapter.submitList(ls)
        binding.list.smoothScrollToPosition(ls.size - 1)
    }

    override fun update(data: ContactData) {
        val ls = adapter.currentList.toMutableList()
        val pos = ls.indexOfFirst { data.id == it.id }
        ls[pos] = data
        adapter.submitList(ls)
        adapter.notifyItemChanged(pos)
    }

    override fun delete(data: ContactData) {
        val ls = adapter.currentList.toMutableList()
        val pos = ls.indexOfFirst { data.id == it.id }
        ls.removeAt(pos)
        adapter.submitList(ls)
    }

    override fun backToLogin() {
        navigate(
            LoginFragment(),
            popBackStack = true,
            popUpToInclusive = true,
            popUpTo = LoginFragment::class.java.simpleName
        )
    }
}