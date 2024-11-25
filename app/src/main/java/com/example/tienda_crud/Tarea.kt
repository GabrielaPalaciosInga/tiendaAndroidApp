package com.example.tienda_crud

data class Tarea(
    var id: String = "",
    var nombre: String = "",
    var apellido: String = "",
    var cedula: String = "",
    var correo: String = "",
){
    fun toMap(): Map<String , String>{
        return mapOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "cedula" to cedula,
            "correo" to correo
        )
    }
}
