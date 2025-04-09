package com.example.controledegastos.ui.features.home

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controledegastos.ui.adapter.MyAdapter
import com.example.controledegastos.listeners.OnClickInterface
import com.example.controledegastos.ui.features.months.MonthsActivity
import com.example.controledegastos.R
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.databinding.ActivityMainBinding
import com.example.controledegastos.ui.features.additem.AddItem
import com.example.controledegastos.ui.features.bardata.BarDataActivity
import com.example.controledegastos.ui.features.help.HelpActivity
import com.example.controledegastos.viewmodel.ItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : OnClickInterface, AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MyAdapter
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private val list = ArrayList<Items>()

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myAdapter = MyAdapter(this@MainActivity)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = myAdapter

        loadAllitems()
        updateTotalValue()

        itemsViewModel.numberBalance.observe(this@MainActivity) {
            binding.toolbarBalance.text = it
            changeToolbarColor(it)
        }

        drawerLayout = binding.drawerLayout
        val navigationView = binding.navView

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {

                    loadAllitems()
                    updateTotalValue()

                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                    true
                }

                R.id.nav_calendar -> {

                    startActivity(Intent(this@MainActivity, MonthsActivity::class.java))

                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.nav_resumo -> {

                    startActivity(Intent(this@MainActivity, BarDataActivity::class.java))

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_inflow -> {

                    loadItemsFlow("Entrada")
                    updateValueFlow("Entrada")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_outflow -> {

                    loadItemsFlow("Saída")
                    updateValueFlow("Saída")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_debts -> {

                    loadItemsCategory("Contas")
                    updateValueCtg("Contas")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_salary -> {

                    loadItemsCategory("Salário")
                    updateValueCtg("Salário")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_investments -> {

                    loadItemsCategory("Investimentos")
                    updateValueCtg("Investimentos")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_extraIncome -> {

                    loadItemsCategory("Rendimentos Extras")
                    updateValueCtg("Rendimentos Extras")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_food -> {

                    loadItemsCategory("Alimentação")
                    updateValueCtg("Alimentação")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_health -> {

                    loadItemsCategory("Saúde")
                    updateValueCtg("Saúde")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_transport -> {

                    loadItemsCategory("Transporte")
                    updateValueCtg("Transporte")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_leisure -> {

                    loadItemsCategory("Lazer e Entretenimento")
                    updateValueCtg("Lazer e Entretenimento")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_edu -> {

                    loadItemsCategory("Educação")
                    updateValueCtg("Educação")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_others -> {

                    loadItemsCategory("Outros")
                    updateValueCtg("Outros")

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_help -> {

                    startActivity(Intent(this@MainActivity, HelpActivity::class.java))

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_exit -> {

                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                    finish()

                }

                else -> false
            }
            true
        }

        binding.floatingActionAddItem.setOnClickListener {
            startActivity(Intent(this, AddItem::class.java).putExtra("type", "Add"))
            onPause()
        }
    }

    fun loadAllitems() {
        itemsViewModel.getAllItems().observe(this@MainActivity) {
            it?.let {
                myAdapter.setItems(it)
            }
        }
    }

    fun loadItemsCategory(itemCtg: String) {
        itemsViewModel.getCategory(itemCtg).observe(this@MainActivity) {
            it?.let {
                myAdapter.setItems(it)
            }
        }
    }

    fun loadItemsFlow(itemFlow: String) {
        itemsViewModel.getIOFiltered(itemFlow).observe(this@MainActivity) {
            it?.let {
                myAdapter.setItems(it)
            }
        }
    }

    fun updateValueCtg(ctg: String) {
        itemsViewModel.updateValueCtg(ctg, list)
    }

    fun updateValueFlow(flow: String) {
        itemsViewModel.updateValueFlow(flow, list)
    }

    fun updateTotalValue() {
        itemsViewModel.updateTotalValue(list)
    }

    fun changeToolbarColor(nfTBalance: String) {
        if (nfTBalance.contains("-")) {
            binding.toolbarBalance.setTextColor(Color.parseColor("#FFEB3B"))
            binding.toolbarBalance.setTypeface(null, Typeface.BOLD)
        } else {
            binding.toolbarBalance.setTextColor(Color.parseColor("#1ff024"))
            binding.toolbarBalance.setTypeface(null, Typeface.BOLD)
        }
    }

    override fun onClickDelete(id: Int) {

        AlertDialog.Builder(this).apply {

            setTitle("Deletar Item")
            setMessage("Deseja deletar este item?")

            setPositiveButton("Sim") { dialogInterface: DialogInterface?, p3: Int ->

                Toast.makeText(context, "Item excluído!", Toast.LENGTH_SHORT).show()

                itemsViewModel.deleteItem(id)
                loadAllitems()
                updateTotalValue()

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

    override fun onStart() {
        super.onStart()
        loadAllitems()
        updateTotalValue()
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}