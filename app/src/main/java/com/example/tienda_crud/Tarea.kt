package com.example.tienda_crud

data class Tarea(
    var id: String = "",
    var nombres: String = "",
    var apellidos: String = "",
    var cedula: String = "",
    var correo: String = "",
){
    fun toMap(): Map<String , String>{
        return mapOf(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "cedula" to cedula,
            "correo" to correo
        )
    }
}
