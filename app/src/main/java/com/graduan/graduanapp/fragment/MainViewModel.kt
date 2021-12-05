package com.graduan.graduanapp.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graduan.graduanapp.ApiServices
import com.graduan.graduanapp.MyApp
import com.graduan.graduanapp.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URI

class MainViewModel : ViewModel() {
    private val users: MutableLiveData<UserModel> by lazy {
        MutableLiveData<UserModel>().also {
            CoroutineScope(IO).launch {
                loadUsers()
            }
        }
    }

    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val response: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            CoroutineScope(IO).launch {
                updateUserProfile(name.value!!)
            }
        }
    }

    fun getUsers(): LiveData<UserModel> {
        return users
    }

    private suspend fun loadUsers() {
        // Do an asynchronous operation to fetch users.
        withContext(Main) {
            users.value = ApiServices._instance.getUserInfo(MyApp.appContext)
        }
    }

    fun saveProfile(): LiveData<String> {
        return response
    }

    private suspend fun updateUserProfile(name: String) {
        withContext(Main) {
            val res = ApiServices._instance.updateUserInfo(MyApp.appContext, name)
            response.value = res!!
        }
    }

    suspend fun uploadImage(uri: String): String {
        var response = ""
        withContext(Main) {
            val myURI = URI(uri)
            val picture = File(myURI)
            val requestFile = picture.asRequestBody("image/*".toMediaTypeOrNull())
            val fileToUpload =
                MultipartBody.Part.createFormData("avatar", picture.name, requestFile)
            val res = ApiServices._instance.uploadUserImage(MyApp.appContext, fileToUpload)
            response = res!!
        }
        return response
    }
}