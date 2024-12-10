package com.example.diftrash.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diftrash.data.local.Lite
import com.example.diftrash.databinding.FragmentProfileBinding
import com.example.diftrash.ui.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
//    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        firebaseAuth = FirebaseAuth.getInstance()

//        val currentUser = firebaseAuth.currentUser

//        if (currentUser != null) {
//            val userEmail = currentUser.email
//            val userName = currentUser.displayName
//
//            binding.textName.text = " $userName"
//            binding.textEmail.text = " $userEmail"
//
//        }
        val sharedPreferences =
            requireContext().getSharedPreferences("namauser", Context.MODE_PRIVATE)

        binding.btnLogout.setOnClickListener {
            // Hapus data login
            val editor = sharedPreferences.edit()
            editor.remove("logged_in_user")
            editor.apply()

            // Arahkan ke LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)

            // Akhiri aktivitas saat ini
            requireActivity().finish()
        }
    }
}
