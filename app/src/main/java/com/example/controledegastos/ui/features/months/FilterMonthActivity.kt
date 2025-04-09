package com.example.controledegastos.ui.features.months

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controledegastos.listeners.OnClickInterface
import com.example.controledegastos.ui.features.help.HelpActivity
import com.example.controledegastos.R
import com.example.controledegastos.databinding.ActivityFilterMonthBinding
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.ui.adapter.MyAdapter
import com.example.controledegastos.ui.features.additem.AddItem
import com.example.controledegastos.viewmodel.ItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterMonthActivity : OnClickInterface, AppCompatActivity() {

    private lateinit var binding: ActivityFilterMonthBinding
    private lateinit var toggleF: ActionBarDrawerToggle
    private lateinit var drawerLayoutF: DrawerLayout
    private lateinit var myAdapter: MyAdapter

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterMonthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadAllitems()

        myAdapter = MyAdapter(this@FilterMonthActivity)
        binding.recyclerViewF.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewF.adapter = myAdapter

        drawerLayoutF = binding.drawerLayoutFilter
        val toolbarF = binding.toolbarF
        val navigationViewFilter = binding.navViewFilter

        toggleF = ActionBarDrawerToggle(
            this@FilterMonthActivity,
            drawerLayoutF,
            toolbarF,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayoutF.addDrawerListener(toggleF)
        toggleF.syncState()

        navigationViewFilter.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_homeF -> {

                    loadAllitems()
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                    true
                }

                R.id.nav_helpF -> {

                    startActivity(Intent(this@FilterMonthActivity, HelpActivity::class.java))
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_inflowF -> {

                    loadAllitemsFlow("Entrada")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_outflowF -> {

                    loadAllitemsFlow("Saída")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_salaryF -> {

                    loadAllitems("Salário")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_investmentsF -> {

                    loadAllitems("Investimentos")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_extraIncomeF -> {

                    loadAllitems("Rendimentos Extras")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_debtsF -> {

                    loadAllitems("Contas")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_foodF -> {

                    loadAllitems("Alimentação")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_healthF -> {

                    loadAllitems("Saúde")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_transportF -> {

                    loadAllitems("Transporte")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_leisureF -> {

                    loadAllitems("Lazer e Entretenimento")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_eduF -> {

                    loadAllitems("Educação")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_othersF -> {

                    loadAllitems("Outros")
                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_deleteF -> {

                    val alertDialog = AlertDialog.Builder(this@FilterMonthActivity)

                    alertDialog.apply {

                        setTitle("Deletar Itens")
                        setMessage("Deseja deletar todos os itens deste mês?")

                        setPositiveButton("Sim") { dialogInterface: DialogInterface?, p3: Int ->

                            Toast.makeText(
                                context, "Itens excluídos com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val month = intent.getStringExtra("month").toString()

                            itemsViewModel.deleteItemMonth(month)
                            loadAllitems()
                            finish()

                            startActivity(
                                Intent(
                                    this@FilterMonthActivity,
                                    MonthsActivity::class.java
                                )
                            )
                        }
                        setNegativeButton("Não") { p1, p2 ->
                            Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
                        }
                    }.create().show()

                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_exitF -> {

                    drawerLayoutF.closeDrawer(GravityCompat.START)
                    drawerLayoutF.closeDrawers()
                    true
                    finish()

                }

                else -> false
            }
            true
        }

        binding.floatingActionButtonBack.setOnClickListener {
            finish()
        }
    }

    private fun loadAllitems(ctg: String? = null) {

        val month = intent.getStringExtra("month").toString()
        itemsViewModel.loadAllitemsFiltered(month, ctg)

        if (ctg != null) {
            itemsViewModel.getMonthCtg(month, ctg).observe(this@FilterMonthActivity) {
                it?.let {
                    myAdapter.setItems(it)
                }
            }
        } else {
            itemsViewModel.getMonth(month).observe(this@FilterMonthActivity) {
                it?.let {
                    myAdapter.setItems(it)
                }
            }
        }

        itemsViewModel.inflowMonth.observe(this) {
            binding.totalInflow.text = it
        }

        itemsViewModel.outflowMonth.observe(this) {
            binding.totalOutflow.text = it
        }

        itemsViewModel.balanceMonth.observe(this) {
            binding.totalBalance.text = it
        }

        if (ctg != null) binding.toolbarTitle.text = "Filtro: " + ctg else
            binding.toolbarTitle.text = "Todos os Lançamentos"

    }

    private fun loadAllitemsFlow(flow: String) {

        val month = intent.getStringExtra("month").toString()
        itemsViewModel.loadAllitemsFlow(flow, month)

        itemsViewModel.inflowTotal.observe(this) {
            it?.let {
                binding.totalInflow.text = it
            }
        }
        itemsViewModel.outflowTotal.observe(this) {
            it?.let {
                binding.totalOutflow.text = it
            }
        }
        itemsViewModel.balanceTotal.observe(this) {
            it?.let {
                binding.totalBalance.text = it
            }
        }
        itemsViewModel.getMonthFlow(month, flow).observe(this@FilterMonthActivity) {
            it?.let {
                myAdapter.setItems(it)
            }
        }

        binding.toolbarTitle.text = "Filtro: $flow"

    }

    override fun onResume() {
        super.onResume()
        loadAllitems()
    }

    override fun onClickDelete(id: Int) {

        AlertDialog.Builder(this).apply {

            setTitle("Deletar Item")
            setMessage("Deseja deletar este item?")

            setPositiveButton("Sim") { dialogInterface: DialogInterface?, p3: Int ->

                Toast.makeText(context, "Item excluído!", Toast.LENGTH_SHORT).show()

                itemsViewModel.deleteItem(id)
                loadAllitems()

            }
            setNegativeButton("Não") { p1, p2 ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }.create().show()

    }

    override fun onClickEdit(items: Items, value: String) {

        AlertDialog.Builder(this).apply {

            setTitle("Editar Item")
            setMessage("Deseja editar este item?")

            setPositiveButton("Sim") { dialogInterface: DialogInterface?, p3: Int ->
                context.startActivity(
                    Intent(context, AddItem::class.java).putExtra("type", "Update")
                        .putExtra("id", items.id)
                        .putExtra("desc", items.description)
                        .putExtra("obs", items.observation)
                        .putExtra("data", "--/--/----")
                        .putExtra("valor", value)
                        .putExtra("pay", items.paymentMethod)
                        .putExtra("io", items.io)
                        .putExtra("ctg", items.category)
                        .putExtra("datevalue", items.date)
                )
            }
            setNegativeButton("Não") { p1, p2 ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
    }

}