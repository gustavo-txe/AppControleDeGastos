package com.example.controledegastos.listeners

import com.example.controledegastos.data.model.Items

interface OnClickInterface {

    fun onClickDelete(id: Int)
    fun onClickEdit(items: Items, value: String)

}