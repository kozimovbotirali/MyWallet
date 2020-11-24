package uz.xdevelop.exammonth3.presentation.mvp.presenters

import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData
import uz.xdevelop.exammonth3.presentation.mvp.contracts.ContactContract
import uz.xdevelop.exammonth3.utils.ThreadsHelper

class ContactPresenter(
    private val model: ContactContract.Model,
    private val view: ContactContract.View
) : ContactContract.Presenter, ThreadsHelper() {

    override fun requestDataFromServer() {
        view.showProgressDialog()
        model.getAllContactsFromServer {
            runOnWorkerThread {
                it.onData {
                    model.deleteAllFromRoom()
                    model.insertAllToRoom(it!!)
                    runOnUiThread {
                        view.initData(it)
                    }
                }
            }

            runOnUiThread {
                it.onFailure {
                    onFailure(it, 1)
                }
                it.onMessage { view.showMessage(it) }
                view.hideProgressDialog()
            }
        }
    }

    override fun createContact(data: ContactData) {
        view.showProgressDialog()
        model.insertToServer(data) {
            runOnWorkerThread {
                it.onData {
                    model.insertToRoom(it)
                    runOnUiThread {
                        view.insert(it!!)
                    }
                }
                runOnUiThread {
                    it.onFailure {
                        onFailure(it)
                    }
                    it.onMessage { view.showMessage(it) }
                    view.hideProgressDialog()
                }
            }
        }
    }

    override fun editContact(data: ContactData) {
        view.showProgressDialog()
        model.updateInServer(data) {
            runOnWorkerThread {
                it.onData {
                    model.updateInRoom(it)
                    runOnUiThread {
                        view.update(it!!)
                    }
                }
            }
            runOnUiThread {
                it.onFailure {
                    onFailure(it)
                }
                it.onMessage { view.showMessage(it) }
                view.hideProgressDialog()
            }
        }
    }

    override fun deleteContact(data: ContactData) {
        view.showProgressDialog()
        model.deleteFromServer(data) {
            runOnWorkerThread {
                it.onData {
                    model.deleteFromRoom(it)
                    runOnUiThread {
                        view.delete(it!!)
                    }
                }
                runOnUiThread {
                    it.onFailure {
                        onFailure(it)
                    }
                    it.onMessage { view.showMessage(it) }
                    view.hideProgressDialog()
                }
            }
        }
    }

    override fun openEditContactDialog(data: ContactData) {
        view.openEditDialog(data)
    }

    override fun openDeleteContactDialog(data: ContactData) {
        view.openDeleteDialog(data)
    }

    override fun refreshClicked() {
        requestDataFromServer()
    }

    override fun addContactClicked() {
        view.openAddDialog()
    }

    override fun logOut() {
        runOnWorkerThread {
            model.logOut()
            runOnUiThread {
                view.backToLogin()
            }
        }
    }

    fun onFailure(t: Throwable, type: Int = 0) {
        view.onResponseFailure(t)
        view.hideProgressDialog()

        if (type == 1) {
            loadLocal()
        }
    }

    private fun loadLocal() {
        runOnWorkerThread {
            val ls = model.getAllFromRoom()
            runOnUiThread {
                view.showMessage("Data loaded from Local DB!")
                view.initData(ls)
                view.hideProgressDialog()
            }
        }
    }
}