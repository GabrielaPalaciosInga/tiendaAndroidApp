package com.example.tienda_crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter (
    var listaTareas : List<Tarea>,
    val onBorrarClick:(String)-> Unit,
    val onActualizarClick: (Tarea) -> Unit,
): RecyclerView.Adapter<TareaAdapter.ViewHolder>()

{
    //Se crean los elementos del xml para poder manipularlos dentro del codigo
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //Card View
        val cvTarea: CardView = itemView.findViewById(R.id.cvTarea)
        val nombres: TextView = itemView.findViewById(R.id.nombres)
        val apellidos: TextView = itemView.findViewById(R.id.apellidos)
        val cedula: TextView = itemView.findViewById(R.id.cedula)
        val correo: TextView = itemView.findViewById(R.id.correo)
        val ibtnBorrar: ImageButton = itemView.findViewById(R.id.ibtnBorrar)


    }
    //Se conecta el XML con el adaptador
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_tarea, parent, false)
        return ViewHolder(view)
    }

    //Se muestra la info y acciones que haran los elementos seg[un sea el caso
    override fun onBindViewHolder(holder: TareaAdapter.ViewHolder, position: Int) {
        val tarea = listaTareas[position]

        holder.nombres.text = tarea.nombres
        holder.apellidos.text = tarea.apellidos
        holder.cedula.text = tarea.cedula
        holder.correo.text = tarea.correo

        //Acciones
        holder.ibtnBorrar.setOnClickListener {
            onBorrarClick(tarea.id)
        }
        holder.cvTarea.setOnClickListener {
            onActualizarClick(tarea)
        }


    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }

}