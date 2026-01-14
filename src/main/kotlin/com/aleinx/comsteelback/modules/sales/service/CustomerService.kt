package com.aleinx.comsteelback.modules.sales.service

import com.aleinx.comsteelback.modules.sales.dto.CreateCustomerRequest
import com.aleinx.comsteelback.modules.sales.model.Customer
import com.aleinx.comsteelback.modules.sales.repository.ClientRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(private val clientRepository: ClientRepository) {

    fun searchCustomers(query: String): List<Customer> {
        return clientRepository.search(query)
    }

    fun createCustomer(req: CreateCustomerRequest): Customer {
        if (clientRepository.existsByDocNumber(req.docNumber)) {
            throw RuntimeException("Ya existe un cliente con el documento ${req.docNumber}")
        }
        return clientRepository.save(
            Customer(
                name = req.name,
                docNumber = req.docNumber,
                email = req.email,
                phone = req.phone,
                address = req.address
            )
        )
    }
}