package com.aleinx.comsteelback.modules.sales.controller

import com.aleinx.comsteelback.modules.sales.dto.CreateCustomerRequest
import com.aleinx.comsteelback.modules.sales.dto.ExternalCustomerDto
import com.aleinx.comsteelback.modules.sales.model.Customer
import com.aleinx.comsteelback.modules.sales.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = ["*"])
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping("/search")
    fun search(@RequestParam query: String): ResponseEntity<List<Customer>> {
        return ResponseEntity.ok(customerService.searchCustomers(query))
    }

    // --- Listado paginado ---
    @GetMapping
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<Page<Customer>> {
        val pageable = PageRequest.of(page, size, Sort.by("id").descending())
        return ResponseEntity.ok(customerService.getAllCustomers(search, pageable))
    }

    @PostMapping
    fun create(@RequestBody req: CreateCustomerRequest): ResponseEntity<Customer> {
        return ResponseEntity.ok(customerService.createCustomer(req))
    }

    // --- Editar ---
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: CreateCustomerRequest): ResponseEntity<Customer> {
        return ResponseEntity.ok(customerService.updateCustomer(id, req))
    }
    @GetMapping("/consult-external/{docNumber}")
    fun consultExternal(@PathVariable docNumber: String): ResponseEntity<ExternalCustomerDto> {
        return ResponseEntity.ok(customerService.consultExternalData(docNumber))
    }
}