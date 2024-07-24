package com.kawunus.glossario.ui.fragments

import android.app.Activity
import android.content.Intent
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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kawunus.glossario.R
import com.kawunus.glossario.data.preferences.UserSharedPreferences
import com.kawunus.glossario.databinding.FragmentProfileBinding
import com.kawunus.glossario.ui.activities.SettingsActivity
import java.io.IOException

class ProfileFragment : Fragment() {
    private lateinit var userSharedPreferences: UserSharedPreferences
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        init()
        emailTextView.text = userSharedPreferences.getNickname()
        val imageUrl = userSharedPreferences.getImage() as String
        if (imageUrl.isNotEmpty()) {
            Glide.with(this@ProfileFragment).load(imageUrl).into(profileImageView)
        }

        profileImageView.setOnClickListener {
            selectImage()
        }
        settinsButton.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init() {
        userSharedPreferences = UserSharedPreferences(requireActivity())
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
                            userSharedPreferences.setImage(downloadUri.toString())
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
                context, getString(R.string.profile_upload_error_not_found), Toast.LENGTH_LONG
            ).show()
        }
    }
}