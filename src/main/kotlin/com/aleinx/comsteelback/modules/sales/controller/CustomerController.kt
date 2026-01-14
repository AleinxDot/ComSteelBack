package com.aleinx.comsteelback.modules.sales.controller

import com.aleinx.comsteelback.modules.sales.dto.CreateCustomerRequest
import com.aleinx.comsteelback.modules.sales.service.CustomerService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = ["*"])
class CustomerController(private val customerService: CustomerService) {

    @GetMapping
    fun search(@RequestParam query: String): ResponseEntity<Any> {
        return ResponseEntity.ok(customerService.searchCustomers(query))
    }

    @PostMapping
    fun create(@Valid @RequestBody req: CreateCustomerRequest): ResponseEntity<Any> {
        val customer = customerService.createCustomer(req)
        return ResponseEntity.ok(mapOf("id" to customer.id, "name" to customer.name))
    }
}