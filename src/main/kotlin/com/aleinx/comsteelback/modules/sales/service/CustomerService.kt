package com.aleinx.comsteelback.modules.sales.service

import com.aleinx.comsteelback.modules.sales.dto.CreateCustomerRequest
import com.aleinx.comsteelback.modules.sales.dto.ExternalCustomerDto
import com.aleinx.comsteelback.modules.sales.model.Customer
import com.aleinx.comsteelback.modules.sales.repository.ClientRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
class CustomerService(
    private val clientRepository: ClientRepository
) {

    // --- Búsqueda simple para autocompletado---
    fun searchCustomers(query: String): List<Customer> {
        return clientRepository.findByNameContainingIgnoreCase(query)
    }

    fun getAllCustomers(search: String?, pageable: Pageable): Page<Customer> {
        return if (search.isNullOrBlank()) {
            clientRepository.findAll(pageable)
        } else {
            clientRepository.findAll(pageable)
        }
    }

    @Transactional
    fun createCustomer(req: CreateCustomerRequest): Customer {
        // Validar duplicados por DNI/RUC si es necesario
        val customer = Customer(
            docNumber = req.docNumber,
            name = req.name,
            address = req.address,
            phone = req.phone,
            email = req.email
        )
        return clientRepository.save(customer)
    }

    @Transactional
    fun updateCustomer(id: Long, req: CreateCustomerRequest): Customer {
        val customer = clientRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Cliente no encontrado")

        customer.docNumber = req.docNumber
        customer.name = req.name
        customer.address = req.address
        customer.phone = req.phone
        customer.email = req.email

        return clientRepository.save(customer)
    }
    // --- Mover a un servicio común si es necesario ---
    fun consultExternalData(docNumber: String): ExternalCustomerDto {
        val restTemplate2 = RestTemplate()

        // CONFIGURACIÓN DE TU PROVEEDOR (Ejemplo Genérico)
        val token = "382b6ccb3268e0df541378dc223184b7e7a19599f83aafb283a920011bc4a40b"
        val type = if (docNumber.length == 11) "ruc" else "dni"
        val url = "https://apiperu.dev/api/$type/$docNumber" // <--- CAMBIA ESTO POR LA URL REAL

        // Headers (casi siempre piden Bearer Token)
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
        headers.set("Content-Type", "application/json")

        val entity = HttpEntity<String>(headers)

        try {
            // 2. Realizar petición y parsear como Mapa genérico
            val response = restTemplate2.exchange(url, HttpMethod.GET, entity, Map::class.java)
            val root = response.body ?: throw RuntimeException("Respuesta vacía del proveedor")

            // 3. Validar éxito (según tu JSON: "success": true)
            if (root["success"] != true) {
                throw RuntimeException("La búsqueda no tuvo éxito o el documento no existe")
            }

            // 4. Extraer el objeto "data"
            @Suppress("UNCHECKED_CAST")
            val data = root["data"] as? Map<String, Any>
                ?: throw RuntimeException("Formato de respuesta inválido: falta 'data'")

            // 5. Mapeo Inteligente (RUC vs DNI)
            // Intentamos leer 'nombre_o_razon_social' (RUC), si es nulo, leemos 'nombre_completo' (DNI)
            val name = (data["nombre_o_razon_social"] as? String)
                ?: (data["nombre_completo"] as? String)
                ?: "NOMBRE DESCONOCIDO"

            // Dirección (A veces viene vacía en DNI, manejamos el caso)
            val address = (data["direccion_completa"] as? String)?.takeIf { it.isNotBlank() }
                ?: (data["direccion"] as? String) // Fallback a dirección simple
                ?: ""

            // Datos extra exclusivos de RUC
            val status = data["estado"] as? String      // Ej: "ACTIVO"
            val condition = data["condicion"] as? String // Ej: "HABIDO"

            // 6. Retornar DTO estandarizado
            return ExternalCustomerDto(
                docNumber = docNumber,
                name = name,
                address = address,
                status = status,
                condition = condition
            )

        } catch (e: Exception) {
            e.printStackTrace()
            // Lanza una excepción controlada para que el Frontend muestre un mensaje limpio
            throw RuntimeException("Error consultando documento: ${e.message}")
        }
    }
}