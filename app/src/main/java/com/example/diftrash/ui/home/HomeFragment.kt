package com.example.diftrash.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diftrash.databinding.FragmentHomeBinding
import com.example.diftrash.ui.article.ArticleActivity
import com.example.diftrash.ui.scan.ScanActivity


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.article.setOnClickListener {
            startActivity(Intent(requireContext(), ArticleActivity::class.java))
        }

        binding.scan.setOnClickListener {
            startActivity(Intent(requireContext(), ScanActivity::class.java))
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}