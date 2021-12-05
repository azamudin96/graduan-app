package com.graduan.graduanapp.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.graduan.graduanapp.AccountManager
import com.graduan.graduanapp.MainActivity
import com.graduan.graduanapp.MyApp
import com.graduan.graduanapp.R
import com.graduan.graduanapp.activity.LoginActivity
import com.graduan.graduanapp.databinding.ProfileFragmentBinding
import com.graduan.graduanapp.utils.LoadingDialog
import com.master.permissionhelper.PermissionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var mainViewModel: MainViewModel

    lateinit var navController: NavController
    private lateinit var profileFragmentBinding: ProfileFragmentBinding
    private var email: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileFragmentBinding = ProfileFragmentBinding.inflate(inflater)
        return profileFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mainViewModel = MainViewModel()

        mainViewModel.getUsers().observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {

                Glide.with(this)
                    .load(response.avatarUrl)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(profileFragmentBinding.imgUser);

                profileFragmentBinding.tvName.text = response.name
                email = response.email
            }
        })

        initUI()
    }

    private fun initUI() {

        profileFragmentBinding.btnEdit.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        profileFragmentBinding.llAboutMe.setOnClickListener {
            val bundle = bundleOf(
                "name" to profileFragmentBinding.tvName.text.toString(),
                "email" to email
            )
            navController.navigate(R.id.action_mainFragment_to_viewEditProfileFragment,bundle)
        }

        profileFragmentBinding.llLogout.setOnClickListener {
            logOut()
        }
    }

    private var mProfileUri: Uri? = null

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                mProfileUri = fileUri

                CoroutineScope(IO).launch {
                    val response = mainViewModel.uploadImage(fileUri.toString())

                    withContext(Main) {
                        if (response == "Success") {
                            Toast.makeText(context, "Successfully Update", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Fail to Update", Toast.LENGTH_LONG).show()
                        }
                    }

                }

                profileFragmentBinding.imgUser.setImageURI(mProfileUri)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    fun logOut() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Logout")
            .setMessage("Are you sure to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, which ->
                CoroutineScope(IO).launch {
                    val response = mainViewModel.signOut()

                    withContext(Main) {
                        if (response == "Success") {
                            AccountManager.instance.setSingleSignOn(false)
                            startActivity(Intent(activity, LoginActivity::class.java))
                            activity!!.finish()
                            Toast.makeText(context, "Successfully Logout", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Fail to logout", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
            .setNegativeButton("No") { dialog, which -> }
        //Creating dialog box
        val dialog = builder.create()
        dialog.show()
    }

}