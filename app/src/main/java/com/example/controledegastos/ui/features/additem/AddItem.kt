package com.example.controledegastos.ui.features.additem

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.controledegastos.data.model.CategoryType
import com.example.controledegastos.data.model.FlowType
import com.example.controledegastos.data.model.Items
import com.example.controledegastos.databinding.ActivityAddItemBinding
import com.example.controledegastos.viewmodel.ItemsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddItem : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private var noteId: Int = 1
    lateinit var IOfinalValue: String
    lateinit var noteIOtext: String
    private lateinit var type: String
    private lateinit var descNote: String
    private lateinit var obsNote: String
    private lateinit var notedate: String
    private lateinit var datevalue: String
    private lateinit var noteValue: String
    private lateinit var noteIO: String
    private lateinit var payMethod: String
    private lateinit var ctg: String
    private var cal = Calendar.getInstance()

    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        type = intent.getStringExtra("type").toString()

        if (type == "Update") {

            noteId = intent.getIntExtra("id", -1)
            descNote = intent.getStringExtra("desc").toString()
            obsNote = intent.getStringExtra("obs").toString()
            notedate = intent.getStringExtra("data").toString()
            noteValue = intent.getStringExtra("valor").toString()
            noteIO = intent.getStringExtra("io").toString()
            ctg = intent.getStringExtra("ctg").toString()
            datevalue = intent.getStringExtra("datevalue").toString()
            payMethod = intent.getStringExtra("pay").toString()

            binding.DescId.setText(descNote)
            binding.ObsId.setText(obsNote)
            binding.textDateSelected.setText(notedate)
            binding.saveNote.setText("EDITAR")
            binding.textViewValor.setText("Valor anterior: $noteValue")
            binding.textViewCategory.setText("Categoria anterior: $ctg")
            binding.editValue.hint = "Digite um novo valor..."
            binding.textViewPay.setText("Método de pagamento anterior: $payMethod")
            binding.textViewDate.setText("Data anterior: $datevalue \n       Clique para editar:")

            if (noteIO == FlowType.OUTFLOW.value) {
                noteIOtext = FlowType.OUTFLOW.value
                binding.textViewFlowEdit.setTextColor(Color.RED)
                binding.spinnerIO.setSelection(1)
            } else {
                noteIOtext = FlowType.INFLOW.value
                binding.textViewFlowEdit.setTextColor(Color.GREEN)
            }

            binding.textViewFlowEdit.setText("Valor anterior: $noteIOtext")
            binding.buttonCancel.visibility = View.VISIBLE
            binding.buttonCancel.setOnClickListener { onBackPressed() }

        }

        val IOarray = FlowType.entries.map { it.value }.toTypedArray()
        var IOSelected: String? = IOarray.first()

        binding.spinnerIO.adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, IOarray)

        binding.spinnerIO.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                IOSelected = IOarray[position]

                if (IOSelected == FlowType.INFLOW.value) {
                    binding.textViewIO.setText(FlowType.INFLOW.value)
                    binding.textViewIO.setTextColor(Color.GREEN)
                } else {
                    binding.textViewIO.setText(FlowType.OUTFLOW.value)
                    binding.textViewIO.setTextColor(Color.RED)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        val paymentArray = arrayOf("Pagamento á vista", "Pagamento á prazo")
        var paymentSelected: String? = paymentArray.first()

        binding.spinnerPayment.adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, paymentArray)

        binding.spinnerPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                paymentSelected = paymentArray[position]

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        val categoryArray = CategoryType.valuesList.toTypedArray()
        var categorySelected: String? = categoryArray.first()

        binding.spinnerCategory.adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, categoryArray)
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                categorySelected = categoryArray[position]

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        openDatePicker()

        binding.saveNote.setOnClickListener {

            val desc = binding.DescId.text.toString()
            val obs = binding.ObsId.text.toString()
            val value = binding.editValue.text.toString().replace(",", ".")

            if (TextUtils.isEmpty(value)) {
                Toast.makeText(this, "Insira um valor válido...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.textDateSelected.text == "--/--/----") {
                Toast.makeText(this, "Insira uma data válida...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalValue = parseSignedValue(value.toDouble(), IOSelected.orEmpty())

            if (type == "Add") {
                saveItem(
                    itemId = 0,
                    desc = desc,
                    obs = obs,
                    io = IOSelected.orEmpty(),
                    payment = paymentSelected.orEmpty(),
                    value = finalValue,
                    category = categorySelected.orEmpty()
                )
                finish()

            } else {

                val alertDialog = AlertDialog.Builder(this)

                alertDialog.apply {

                    setTitle("Editar Item")
                    setMessage("Deseja confirmar todas as alterações?")

                    setPositiveButton("Sim") { dialogInterface: DialogInterface?, p3: Int ->
                        saveItem(
                            itemId = noteId,
                            desc = desc,
                            obs = obs,
                            io = IOSelected.orEmpty(),
                            payment = paymentSelected.orEmpty(),
                            value = finalValue,
                            category = categorySelected.orEmpty()
                        )
                        finish()
                    }
                    setNegativeButton("Não") { p1, p2 ->
                        Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }.create().show()
            }
        }

        binding.backIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun parseSignedValue(input: Double, selectedFlow: String): Double {
        return if (selectedFlow == FlowType.OUTFLOW.value) -input else input
    }

    private fun saveItem(
        itemId: Int,
        desc: String,
        obs: String,
        io: String,
        payment: String,
        value: Double,
        category: String
    ) {
        IOfinalValue = if (io == FlowType.INFLOW.value) FlowType.INFLOW.value else FlowType.OUTFLOW.value

        val model = Items(
            itemId,
            desc,
            obs,
            IOfinalValue,
            payment,
            value,
            date = binding.textDateSelected.text.toString(),
            cal.get(Calendar.MONTH).toString(),
            category
        )

        if (type == "Add") {
            itemsViewModel.insertItem(model)
        } else {
            itemsViewModel.updateItem(model)
        }
    }

    fun openDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        binding.textViewDate.setOnClickListener {
            DatePickerDialog(
                this@AddItem,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.textDateSelected.setOnClickListener {
            DatePickerDialog(
                this@AddItem,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale("pt", "BR"))
        binding.textDateSelected.text = sdf.format(cal.time)
    }

}
