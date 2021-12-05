package com.graduan.graduanapp.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.graduan.graduanapp.R
import com.graduan.graduanapp.databinding.EditProfileFragmentBinding
import com.graduan.graduanapp.databinding.ProfileFragmentBinding
import com.graduan.graduanapp.utils.LoadingDialog

class EditProfile : Fragment() {

    companion object {
        fun newInstance() = EditProfile()
    }

    lateinit var name: String
    lateinit var email: String

    private lateinit var mainViewModel: MainViewModel
    private lateinit var editProfileFragmentBinding: EditProfileFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = arguments!!.getString("name").toString()
        email = arguments!!.getString("email").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editProfileFragmentBinding = EditProfileFragmentBinding.inflate(inflater)
        return editProfileFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = MainViewModel()
        initUI()
    }

    private fun initUI() {
        editProfileFragmentBinding.etName.setText(name)
        editProfileFragmentBinding.etEmail.setText(email)

        editProfileFragmentBinding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        editProfileFragmentBinding.btnSave.setOnClickListener {

            if (TextUtils.isEmpty(editProfileFragmentBinding.etName.text.toString())) {
                Toast.makeText(
                    context,
                    R.string.errorMessage_noValue,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            LoadingDialog.getInstance().with(context).isLoading
            mainViewModel.name.value = editProfileFragmentBinding.etName.text.toString()
            mainViewModel.saveProfile().observe(viewLifecycleOwner, Observer { response ->
                if (response == "Success") {
                    Toast.makeText(context, "Successfully Update", Toast.LENGTH_LONG).show()
                    activity!!.currentFocus!!.clearFocus()
                } else {
                    Toast.makeText(context, "Fail to Update", Toast.LENGTH_LONG).show()
                }

                LoadingDialog.getInstance().with(context).dismissLoading()

            })
        }
    }
}