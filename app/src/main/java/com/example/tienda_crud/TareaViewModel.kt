package com.example.tienda_crud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.UUID

class TareaViewModel : ViewModel() {
    private val db = Firebase.firestore

    private var _listaTareas = MutableLiveData<List<Tarea>>(emptyList())
    val listaTareas:LiveData<List<Tarea>> = _listaTareas

    init {
        obtenerTareas()
    }
    //obtener tareas de la base de datso
    fun obtenerTareas(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultado = db.collection("tareas").get().await()

                val tareas = resultado.documents.mapNotNull{it.toObject(Tarea::class.java)}
                _listaTareas.postValue(tareas)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    //Agregar tareas de la base de datso
    fun agregarTarea(tarea: Tarea){
        tarea.id = UUID.randomUUID().toString()

        viewModelScope.launch(Dispatchers.IO){
            try {
                db.collection("tareas").document(tarea.id).set(tarea).await()
                _listaTareas.postValue(_listaTareas.value?.plus(tarea))
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    //Agregar tareas de la base de datso
    fun actualizarTarea(tarea: Tarea){
        viewModelScope.launch(Dispatchers.IO){
            try {
                db.collection("tareas").document(tarea.id).update(tarea.toMap()).await()
                _listaTareas.postValue(_listaTareas.value?.map { if(it.id == tarea.id) tarea else it })
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    //Agregamos funcion borrar
    fun borrarTarea(id : String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                db.collection("tareas").document(id).delete().await()
                _listaTareas.postValue(_listaTareas.value?.filter { it.id != id})
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}