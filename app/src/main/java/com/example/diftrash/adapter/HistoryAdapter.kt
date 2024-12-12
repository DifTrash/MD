package com.example.diftrash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diftrash.R
import com.example.diftrash.data.retrofit.DataItem
import com.example.diftrash.ui.history.HistoryFragment

class HistoryAdapter(historyFragment: HistoryFragment) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var dataList: MutableList<DataItem?> = mutableListOf()

    fun updateData(newData: List<DataItem?>?) {
        newData?.let {
            dataList.addAll(0, it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList?.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val confidenceTextView: TextView = itemView.findViewById(R.id.confidenceTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)


        fun bind(item: DataItem?) {
            item?.let {
                val confidence = item.confidence as? Double
                val confidencePercentage = confidence?.times(100)

                val formattedConfidence = String.format("%.2f%%", confidencePercentage)
                val type = item.type
                confidenceTextView.text = "Result: $formattedConfidence"
                typeTextView.text = "Type: $type"

            }
        }
    }
}