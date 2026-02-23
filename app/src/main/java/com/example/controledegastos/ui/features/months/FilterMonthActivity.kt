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
import com.example.controledegastos.data.model.CategoryType
import com.example.controledegastos.data.model.FlowType
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

    private val month: String by lazy { intent.getStringExtra("month").toString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterMonthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myAdapter = MyAdapter(this@FilterMonthActivity)
        binding.recyclerViewF.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewF.adapter = myAdapter

        setupObservers()
        setupDrawer()

        itemsViewModel.applyMonthFilter(month)
        binding.toolbarTitle.text = "Todos os Lançamentos"

        binding.floatingActionButtonBack.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        itemsViewModel.monthItems.observe(this) {
            myAdapter.setItems(it)
        }

        itemsViewModel.monthTotals.observe(this) {
            binding.totalInflow.text = it.inflow
            binding.totalOutflow.text = it.outflow
            binding.totalBalance.text = it.balance
        }
    }

    private fun setupDrawer() {
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
                    itemsViewModel.applyMonthFilter(month)
                    binding.toolbarTitle.text = "Todos os Lançamentos"
                }

                R.id.nav_helpF -> startActivity(Intent(this@FilterMonthActivity, HelpActivity::class.java))
                R.id.nav_inflowF -> applyMonthFlow(FlowType.INFLOW.value)
                R.id.nav_outflowF -> applyMonthFlow(FlowType.OUTFLOW.value)
                R.id.nav_salaryF -> applyMonthCategory(CategoryType.SALARY.value)
                R.id.nav_investmentsF -> applyMonthCategory(CategoryType.INVESTMENTS.value)
                R.id.nav_extraIncomeF -> applyMonthCategory(CategoryType.EXTRA_INCOME.value)
                R.id.nav_debtsF -> applyMonthCategory(CategoryType.DEBTS.value)
                R.id.nav_foodF -> applyMonthCategory(CategoryType.FOOD.value)
                R.id.nav_healthF -> applyMonthCategory(CategoryType.HEALTH.value)
                R.id.nav_transportF -> applyMonthCategory(CategoryType.TRANSPORT.value)
                R.id.nav_leisureF -> applyMonthCategory(CategoryType.LEISURE.value)
                R.id.nav_eduF -> applyMonthCategory(CategoryType.EDUCATION.value)
                R.id.nav_othersF -> applyMonthCategory(CategoryType.OTHERS.value)
                R.id.nav_deleteF -> showDeleteMonthDialog()
                R.id.nav_exitF -> finish()
                else -> return@setNavigationItemSelectedListener false
            }
            drawerLayoutF.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun applyMonthFlow(flow: String) {
        itemsViewModel.applyMonthFilter(month, flow = flow)
        binding.toolbarTitle.text = "Filtro: $flow"
    }

    private fun applyMonthCategory(category: String) {
        itemsViewModel.applyMonthFilter(month, category = category)
        binding.toolbarTitle.text = "Filtro: $category"
    }

    private fun showDeleteMonthDialog() {
        AlertDialog.Builder(this@FilterMonthActivity).apply {
            setTitle("Deletar Itens")
            setMessage("Deseja deletar todos os itens deste mês?")

            setPositiveButton("Sim") { dialogInterface: DialogInterface?, p3: Int ->
                Toast.makeText(context, "Itens excluídos com sucesso!", Toast.LENGTH_SHORT).show()

                itemsViewModel.deleteItemMonth(month)
                finish()
                startActivity(Intent(this@FilterMonthActivity, MonthsActivity::class.java))
            }
            setNegativeButton("Não") { p1, p2 ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
    }

    override fun onResume() {
        super.onResume()
        itemsViewModel.applyMonthFilter(month)
    }

    override fun onClickDelete(id: Int) {

        AlertDialog.Builder(this).apply {

            setTitle("Deletar Item")
            setMessage("Deseja deletar este item?")

            setPositiveButton("Sim") { dialogInterface: DialogInterface?, p3: Int ->

                Toast.makeText(context, "Item excluído!", Toast.LENGTH_SHORT).show()

                itemsViewModel.deleteItem(id)
                itemsViewModel.applyMonthFilter(month)

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
