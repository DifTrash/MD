package com.example.diftrash.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.diftrash.databinding.FragmentInputLoginBinding
import com.example.diftrash.ui.LoginActivity
import com.example.diftrash.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class InputLoginFragment : Fragment() {

    private var _binding: FragmentInputLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPasswordToggle.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        val exception = task.exception
                        val errorMessage = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                "Email/Password yang dimasukkan salah."
                            }
                            else -> {
                                "Terjadi kesalahan, coba lagi."
                            }
                        }
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.txtCreateAccount.setOnClickListener {
            (activity as LoginActivity).navigateToSignupFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
