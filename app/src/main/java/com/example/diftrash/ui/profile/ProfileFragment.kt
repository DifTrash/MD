package com.example.diftrash.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.diftrash.databinding.FragmentProfileBinding
import com.example.diftrash.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        if (currentUser != null) {
            binding.textName.text = currentUser.displayName
            binding.textEmail.text = currentUser.email
        }


        binding.btnLogout.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun logout() {
        firebaseAuth.signOut()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
        startActivity(intent)
    }
}
