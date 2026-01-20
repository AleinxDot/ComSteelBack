package com.aleinx.comsteelback.modules.inventory.service

import com.aleinx.comsteelback.modules.inventory.dto.ExternalSupplierDto
import com.aleinx.comsteelback.modules.inventory.model.Supplier
import com.aleinx.comsteelback.modules.inventory.repository.SupplierRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

@Service
class SupplierService(
    private val supplierRepository: SupplierRepository
) {

    fun getAll(pageable: Pageable): Page<Supplier> {
        return supplierRepository.findAll(pageable)
    }

    fun getAllList(): List<Supplier> {
        return supplierRepository.findAll(Sort.by("name"))
    }

    fun save(supplier: Supplier): Supplier {
        return supplierRepository.save(supplier)
    }

    fun findById(id: Long): Supplier {
        return supplierRepository.findById(id).orElseThrow { RuntimeException("Proveedor no encontrado") }
    }

    // --- LÓGICA DE CONSULTA EXTERNA (IGUAL A CLIENTES) ---
    fun consultExternalData(docNumber: String): ExternalSupplierDto {
        val restTemplate = RestTemplate()

        // RECUERDA: Pon aquí tu TOKEN y URL reales
        val token = "382b6ccb3268e0df541378dc223184b7e7a19599f83aafb283a920011bc4a40b"
        val type = if (docNumber.length == 11) "ruc" else "dni"
        val url = "https://apiperu.dev/api/$type/$docNumber"

        // Headers (casi siempre piden Bearer Token)
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
        headers.set("Content-Type", "application/json")

        val entity = HttpEntity<String>(headers)

        try {
            val response = restTemplate.exchange(url, HttpMethod.GET, entity, Map::class.java)
            val root = response.body ?: throw RuntimeException("Respuesta vacía")

            if (root["success"] != true) {
                throw RuntimeException("Documento no encontrado o inválido")
            }

            @Suppress("UNCHECKED_CAST")
            val data = root["data"] as? Map<String, Any>
                ?: throw RuntimeException("Formato inválido")

            // Prioridad: Razón Social (RUC) -> Nombre Completo (DNI)
            val name = (data["nombre_o_razon_social"] as? String)
                ?: (data["nombre_completo"] as? String)
                ?: "DESCONOCIDO"

            val address = (data["direccion_completa"] as? String)?.takeIf { it.isNotBlank() }
                ?: (data["direccion"] as? String)
                ?: ""

            return ExternalSupplierDto(
                docNumber = docNumber,
                name = name,
                address = address,
                status = data["estado"] as? String,
                condition = data["condicion"] as? String
            )

        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error consultando API: ${e.message}")
        }
    }
}