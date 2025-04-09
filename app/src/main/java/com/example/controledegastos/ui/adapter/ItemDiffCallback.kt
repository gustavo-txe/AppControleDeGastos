package com.example.controledegastos.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.controledegastos.data.model.Items

class ItemDiffCallback(
    private val oldList: List<Items>,
    private val newList: List<Items>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}