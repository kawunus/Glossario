package com.kawunus.glossario

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kawunus.glossario.databinding.FragmentProfileBinding
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: FragmentProfileBinding
    private lateinit var filepath: Uri
    private val pickImageActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val uri = result.data?.data!!
                filepath = uri


                try {
                    val thumbnailSize = Size(300, 300)
                    val bitmap: Bitmap =
                        requireContext().contentResolver.loadThumbnail(uri, thumbnailSize, null)
                    binding.profileImageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                uploadImage()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        init()
        emailTextView.text = prefs.getString(
            ProfileKeys.USER_NICKNAME, ProfileKeys.UserStatus.NOT_REGISTER
        )
        val imageUrl = prefs.getString(ProfileKeys.USER_IMAGE, "") as String
        if (imageUrl.isNotEmpty()) {
            Glide.with(this@Profile).load(imageUrl).into(profileImageView)
        }

        profileImageView.setOnClickListener {
            selectImage()
        }
    }

    private fun init() {
        activity?.let {
            prefs = it.getSharedPreferences("profile", Context.MODE_PRIVATE)
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        pickImageActivityResultLauncher.launch(intent)
    }

    private fun uploadImage() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val storageReference = FirebaseStorage.getInstance().getReference("images/$uid")
            storageReference.putFile(filepath).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context, getString(R.string.profile_upload_sucsess), Toast.LENGTH_LONG
                    ).show()
                    storageReference.downloadUrl.addOnCompleteListener { urlTask ->
                        if (urlTask.isSuccessful) {
                            val downloadUri = urlTask.result
                            FirebaseDatabase.getInstance().getReference("users").child(uid)
                                .child("profileImage").setValue(downloadUri.toString())
                            prefs.edit(commit = true) {
                                putString(ProfileKeys.USER_IMAGE, downloadUri.toString())
                            }
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.profile_upload_error_get_url),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context, getString(R.string.profile_upload_error_get), Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                context,
                getString(R.string.profile_upload_error_not_found), Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = Profile().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}