package com.example.tienda_crud

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tienda_crud.databinding.ActivityGpsKolinBinding
import com.google.android.gms.location.*

class GpsKolin : AppCompatActivity() {
    private lateinit var binding: ActivityGpsKolinBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val CODIGO_PERMISO_UBICACION = 101
    private val CODIGO_PERMISO_SEGUNDO_PLANO = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpsKolinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verificarPermisos()
    }

    private fun verificarPermisos() {
        val permisos = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Permiso de ubicación en segundo plano (Android 10+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permisos.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        val permisosArray = permisos.toTypedArray()
        if (tienePermisos(permisosArray)) {
            onPermisosConcedidos()
        } else {
            // Solicita los permisos de ubicación en primer plano primero
            solicitarPermisos(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), CODIGO_PERMISO_UBICACION
            )
        }
    }

    private fun tienePermisos(permisos: Array<String>): Boolean {
        return permisos.all { permiso ->
            ContextCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun solicitarPermisos(permisos: Array<String>, requestCode: Int) {
        if (permisos.any { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }) {
            mostrarExplicacionPermisos {
                ActivityCompat.requestPermissions(this, permisos, requestCode)
            }
        } else {
            ActivityCompat.requestPermissions(this, permisos, requestCode)
        }
    }

    private fun mostrarExplicacionPermisos(onAceptar: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Permisos requeridos")
            .setMessage("Esta aplicación necesita permisos de ubicación para funcionar correctamente.")
            .setPositiveButton("Aceptar") { _, _ -> onAceptar() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun onPermisosConcedidos() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    imprimirUbicacion(location)
                } else {
                    Toast.makeText(this, "No se puede obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                30000
            ).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                setWaitForAccurateLocation(true)
            }.build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.forEach { location ->
                        imprimirUbicacion(location)
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("GPS", "Error al obtener ubicación: ${e.message}")
        }
    }

    private fun imprimirUbicacion(ubicacion: Location) {
        binding.tvLatitud.text = "${ubicacion.latitude}"
        binding.tvLongitud.text = "${ubicacion.longitude}"
        Log.d("GPS", "LAT: ${ubicacion.latitude} - LON: ${ubicacion.longitude}")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CODIGO_PERMISO_UBICACION -> {
                val permisosConcedidos = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
                if (permisosConcedidos) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                        !tienePermisos(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                    ) {
                        solicitarPermisos(
                            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                            CODIGO_PERMISO_SEGUNDO_PLANO
                        )
                    } else {
                        onPermisosConcedidos()
                    }
                } else {
                    Toast.makeText(this, "Permisos de ubicación denegados.", Toast.LENGTH_SHORT).show()
                }
            }

            CODIGO_PERMISO_SEGUNDO_PLANO -> {
                val permisosConcedidos = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
                if (permisosConcedidos) {
                    onPermisosConcedidos()
                } else {
                    Toast.makeText(
                        this,
                        "Permisos de ubicación en segundo plano denegados.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
