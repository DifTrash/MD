package com.example.diftrash.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.diftrash.R
import com.example.diftrash.data.local.Lite
import com.example.diftrash.databinding.FragmentSignupBinding
import com.example.diftrash.ui.login.InputLoginFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//Deklarasi Binding
        val binding = FragmentSignupBinding.inflate(inflater, container, false)
//Panggil class Lite
        val lite = Lite(requireContext())
//        Event Button SignUp
        binding.btnSignup.setOnClickListener {
            val fullname = binding.inputFullName.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPasswordToggle.text.toString().trim()

            // Validasi input kosong
            if (fullname.isEmpty()) {
                binding.inputFullName.error = "Fullname harus diisi"
                binding.inputFullName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.inputEmail.error = "Email harus diisi"
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }

            // Validasi format email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmail.error = "Format email tidak valid"
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }


            if (password.isEmpty()) {
                binding.inputPasswordToggle.error = "Password harus diisi"
                binding.inputPasswordToggle.requestFocus()
                return@setOnClickListener
            }

            // Validasi apakah email sudah terdaftar
            if (lite.isEmailExists(email)) {
                binding.inputEmail.error = "Email sudah terdaftar"
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }

            // Validasi apakah username sudah digunakan
            if (lite.isUsernameExists(fullname)) {
                binding.inputFullName.error = "Fullname sudah digunakan"
                binding.inputFullName.requestFocus()
                return@setOnClickListener
            }

            // Jika semua validasi lolos, buat pengguna baru
            val pengguna = Lite.Pengguna(fullname, email, password)
            val hasil = lite.insertPengguna(pengguna)
            if (hasil != -1L) {
                Toast.makeText(
                    requireContext(),
                    "Sign Up Berhasil, Silakan Login",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frm_container_login, InputLoginFragment()).commit()
            } else {
                Toast.makeText(requireContext(), "Gagal memasukkan data", Toast.LENGTH_SHORT).show()
            }
        }
        binding.txtLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.frm_container_login, SignupFragment()).commit()
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}