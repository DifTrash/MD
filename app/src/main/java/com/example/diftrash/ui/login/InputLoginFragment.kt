package com.example.diftrash.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.diftrash.R
import com.example.diftrash.data.local.Lite
import com.example.diftrash.databinding.FragmentInputLoginBinding
import com.example.diftrash.ui.MainActivity
import com.example.diftrash.ui.signup.SignupFragment

class InputLoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        Buat Variable Binding
        val binding = FragmentInputLoginBinding.inflate(inflater, container, false)
//        Panggilan class Lite
        val lite = Lite(requireContext())


//        Buat Even Login
        binding.btnLogin.setOnClickListener {
            var email = binding.inputEmail.text.toString()
            val pass = binding.inputPasswordToggle.text.toString()
//            Kondisi Jika Input Kosong
            if (email.isEmpty() || pass.isEmpty()){
                Toast.makeText(requireContext(), "Email/Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }
            // Periksa apakah username terdaftar
            if (!lite.isEmailExists(email)) {
                Toast.makeText(requireContext(), "Email belum terdaftar", Toast.LENGTH_SHORT).show()
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            } else {
                val loginSukses = lite.login(email, pass)
//            Kondisi Jika login berhasil
                if (loginSukses){
                    val sharedPreferences = requireContext().getSharedPreferences("namauser", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    // Simpan nama pengguna yang login
                    editor.putString("logged_in_user", email)
                    editor.apply()
                    Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
//            Kondisi Jika login gagal
                else{
                    binding.inputPasswordToggle.error = "Password Salah"
                    binding.inputPasswordToggle.requestFocus()
                    return@setOnClickListener
                }
            }}

//          Buat Event SignUp
        binding.txtCreateAccount.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.frm_container_login, SignupFragment()).commit()
        }
        return binding.root
    }

}