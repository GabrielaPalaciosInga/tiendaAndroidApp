package com.example.tienda_crud

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tienda_crud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TareaAdapter
    private lateinit var viewModel: TareaViewModel

    var tareaEdit = Tarea()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TareaViewModel::class.java]

        viewModel.listaTareas.observe(this) { tareas ->
            setupRecyclerView(tareas)
        }

        binding.btnAgregarTarea.setOnClickListener {
            val tarea = Tarea(
                nombre = binding.etNombre.text.toString(),
                apellido = binding.etApellido.text.toString(),
                cedula = binding.etCedula.text.toString(),
                correo = binding.etCorreo.text.toString()
            )

            viewModel.agregarTarea(tarea)

            binding.etNombre.setText("")
            binding.etApellido.setText("")
            binding.etCedula.setText("")
            binding.etCorreo.setText("")
        }

        binding.btnActualizarTarea.setOnClickListener {
            tareaEdit.nombre = ""
            tareaEdit.apellido = ""
            tareaEdit.cedula = ""
            tareaEdit.correo = ""

            tareaEdit.nombre = binding.etNombre.text.toString()
            tareaEdit.apellido = binding.etApellido.text.toString()
            tareaEdit.cedula = binding.etCedula.text.toString()
            tareaEdit.correo = binding.etCorreo.text.toString()

            viewModel.actualizarTarea(tareaEdit)

            adapter.notifyDataSetChanged()

            binding.etNombre.setText("")
            binding.etApellido.setText("")
            binding.etCedula.setText("")
            binding.etCorreo.setText("")
        }
    }

    fun setupRecyclerView(listaTareas: List<Tarea>) {
        adapter = TareaAdapter(listaTareas, ::borrarTarea, ::actualizarTarea)
        binding.rvTareas.adapter = adapter
    }

    fun borrarTarea(id: String) {
        viewModel.borrarTarea(id)
    }

    fun actualizarTarea(tarea: Tarea) {
        tareaEdit = tarea

        binding.etNombre.setText(tareaEdit.nombre)
        binding.etApellido.setText(tareaEdit.apellido)
        binding.etCedula.setText(tareaEdit.cedula)
        binding.etCorreo.setText(tareaEdit.correo)
    }
}