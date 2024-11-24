package com.example.tienda_crud

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tienda_crud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TareaAdapter
    private lateinit var viewModel: TareaViewModel

    private var tareaEdit = Tarea()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TareaViewModel::class.java]

        viewModel.listaTareas.observe(this){ tareas->
            setupRecyclerView(tareas)
        }
        //Se definen las acciones de los botones editar y borrar
        binding.buttonAdd.setOnLongClickListener {
            val tarea = Tarea(
                nombres = binding.etNombres.text.toString(),
                apellidos = binding.etApellidos.text.toString(),
                cedula = binding.etCedula.text.toString(),
                correo = binding.etCorreo.text.toString()
            )

            viewModel.agregarTarea(tarea)

            binding.etNombres.setText("")
            binding.etApellidos.setText("")
            binding.etCedula.setText("")
            binding.etCorreo.setText("")
        }

        //Actualizar tarea
        binding.buttonUpdate.setOnLongClickListener{
            tareaEdit.nombres=""
            tareaEdit.apellidos=""
            tareaEdit.cedula=""
            tareaEdit.correo=""

            tareaEdit.nombres = binding.etNombres.text.toString()
            tareaEdit.apellidos = binding.etApellidos.text.toString()
            tareaEdit.cedula = binding.etCedula.text.toString()
            tareaEdit.correo = binding.etCorreo.text.toString()

            viewModel.actualizarTarea(tareaEdit)

            adapter.notifyDataSetChanged()

            binding.etNombres.setText("")
            binding.etApellidos.setText("")
            binding.etCedula.setText("")
            binding.etCorreo.setText("")

        }
    }

    private fun setupRecyclerView(listaTareas: List<Tarea>){
        adapter = TareaAdapter(listaTareas, :: borrarTarea, ::actualizarTarea)
        binding.rvTareas.adapter = adapter
    }

    private fun borrarTarea(id: String){
        viewModel.borrarTarea(id)
    }

    private fun actualizarTarea(tarea: Tarea){
        tareaEdit = tarea

        binding.etNombres.setText(tareaEdit.nombres)
        binding.etApellidos.setText(tareaEdit.apellidos)
        binding.etCedula.setText(tareaEdit.cedula)
        binding.etCorreo.setText(tareaEdit.correo)
    }


}
