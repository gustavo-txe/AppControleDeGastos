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
import com.example.controledegastos.R
import com.example.controledegastos.data.model.CategoryType
import com.example.controledegastos.data.model.FlowType
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.databinding.ActivityMainBinding
import com.example.controledegastos.listeners.OnClickInterface
import com.example.controledegastos.ui.adapter.MyAdapter
import com.example.controledegastos.ui.features.additem.AddItem
import com.example.controledegastos.ui.features.bardata.BarDataActivity
import com.example.controledegastos.ui.features.help.HelpActivity
import com.example.controledegastos.ui.features.months.MonthsActivity
import com.example.controledegastos.viewmodel.ItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : OnClickInterface, AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MyAdapter
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myAdapter = MyAdapter(this@MainActivity)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = myAdapter

        setupObservers()
        setupDrawer()

        itemsViewModel.applyMainAllFilter()

        binding.floatingActionAddItem.setOnClickListener {
            startActivity(Intent(this, AddItem::class.java).putExtra("type", "Add"))
        }
    }

    private fun setupObservers() {
        itemsViewModel.mainItems.observe(this) { items ->
            myAdapter.setItems(items)
        }

        itemsViewModel.numberBalance.observe(this@MainActivity) {
            binding.toolbarBalance.text = it
            changeToolbarColor(it)
        }
    }

    private fun setupDrawer() {
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
                R.id.nav_home -> itemsViewModel.applyMainAllFilter()
                R.id.nav_calendar -> startActivity(Intent(this@MainActivity, MonthsActivity::class.java))
                R.id.nav_resumo -> startActivity(Intent(this@MainActivity, BarDataActivity::class.java))
                R.id.nav_inflow -> itemsViewModel.applyMainFlowFilter(FlowType.INFLOW.value)
                R.id.nav_outflow -> itemsViewModel.applyMainFlowFilter(FlowType.OUTFLOW.value)
                R.id.nav_debts -> itemsViewModel.applyMainCategoryFilter(CategoryType.DEBTS.value)
                R.id.nav_salary -> itemsViewModel.applyMainCategoryFilter(CategoryType.SALARY.value)
                R.id.nav_investments -> itemsViewModel.applyMainCategoryFilter(CategoryType.INVESTMENTS.value)
                R.id.nav_extraIncome -> itemsViewModel.applyMainCategoryFilter(CategoryType.EXTRA_INCOME.value)
                R.id.nav_food -> itemsViewModel.applyMainCategoryFilter(CategoryType.FOOD.value)
                R.id.nav_health -> itemsViewModel.applyMainCategoryFilter(CategoryType.HEALTH.value)
                R.id.nav_transport -> itemsViewModel.applyMainCategoryFilter(CategoryType.TRANSPORT.value)
                R.id.nav_leisure -> itemsViewModel.applyMainCategoryFilter(CategoryType.LEISURE.value)
                R.id.nav_edu -> itemsViewModel.applyMainCategoryFilter(CategoryType.EDUCATION.value)
                R.id.nav_others -> itemsViewModel.applyMainCategoryFilter(CategoryType.OTHERS.value)
                R.id.nav_help -> startActivity(Intent(this@MainActivity, HelpActivity::class.java))
                R.id.nav_exit -> finish()
                else -> return@setNavigationItemSelectedListener false
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
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
                itemsViewModel.refreshMainBalance()

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
        itemsViewModel.refreshMainBalance()
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
