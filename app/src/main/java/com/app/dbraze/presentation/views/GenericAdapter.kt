package com.app.dbraze.presentation.views

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<T, VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind: (VB, T) -> Unit,
    private val itemClickListener: (T) -> Unit
) : RecyclerView.Adapter<GenericAdapter<T, VB>.ViewHolder>() {

    private var items: List<T> = emptyList()

    fun setItems(newItems: List<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        item?.let {
            bind(holder.binding, item)
            holder.itemView.setOnClickListener {
                itemClickListener(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}

