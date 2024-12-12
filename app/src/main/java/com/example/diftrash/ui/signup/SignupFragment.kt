package com.example.diftrash.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.diftrash.databinding.FragmentSignupBinding
import com.example.diftrash.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Patterns
import java.util.regex.Pattern

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPasswordToggle.text.toString()
            val confirmPassword = binding.passwordConf.text.toString()

            // Cek format email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!containsNumber(password)) {
                Toast.makeText(requireContext(), "Password harus mengandung angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah password dan konfirmasi password cocok
            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lanjutkan dengan proses sign-up jika valid
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Signup successful", Toast.LENGTH_SHORT).show()
                        (activity as LoginActivity).navigateToLoginFragment()
                    } else {
                        Toast.makeText(requireContext(), "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.txtLogin.setOnClickListener {
            (activity as LoginActivity).navigateToLoginFragment()
        }
    }

    private fun containsNumber(password: String): Boolean {
        val pattern: Pattern = Pattern.compile(".*\\d.*")
        return pattern.matcher(password).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
