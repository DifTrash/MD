package com.example.diftrash.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.diftrash.R
import com.example.diftrash.adapter.HistoryAdapter
import com.example.diftrash.data.retrofit.GetResponse
import com.example.diftrash.retrofit.ApiConfig
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL).apply {
            reverseLayout = true
        }

        historyAdapter = HistoryAdapter(this)
        recyclerView.adapter = historyAdapter

        fetchDataFromApi()

        return view
    }

    private fun fetchDataFromApi() {
        val apiService = ApiConfig.getApiService()
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            // Mengambil data dengan UID
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    apiService.getPrediction(uid).enqueue(object : Callback<GetResponse> {
                        override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                            if (response.isSuccessful) {
                                historyAdapter.updateData(response.body()?.data)
                                recyclerView.post {
                                    recyclerView.scrollToPosition(historyAdapter.itemCount - 1)
                                }
                            } else {
                                // Handle jika respons tidak sukses
                                Log.e("HistoryFragment", "API failed to respond successfully: ${response.message()}")
                                Toast.makeText(context, "API gagal merespon", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                            Log.e("HistoryFragment", "Connection failure: ${t.message}")
                            Toast.makeText(context, "Kegagalan koneksi", Toast.LENGTH_SHORT).show()
                        }
                    })
                } catch (e: Exception) {
                    Log.e("HistoryFragment", "Exception: ${e.message}")
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e("HistoryFragment", "User not logged in")
            Toast.makeText(context, "Silahkan login lagi", Toast.LENGTH_SHORT).show()
        }
    }
}
