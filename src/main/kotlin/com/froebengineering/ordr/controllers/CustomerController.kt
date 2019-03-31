package com.froebengineering.ordr.controllers

import com.froebengineering.ordr.domain.Customer
import com.froebengineering.ordr.domain.CustomerType
import com.froebengineering.ordr.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Suppress("unused")
@RestController
@RequestMapping(value = ["/customer"])
class CustomerController @Autowired constructor(val customerRepository: CustomerRepository) {

  @PostMapping("/")
  fun create(@RequestParam name: String, @RequestParam customerType: CustomerType) : ResponseEntity<Customer>{
    val newCustomer = Customer(name, customerType)
    return ResponseEntity.ok(customerRepository.save(newCustomer))
  }
  
  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): ResponseEntity<Customer>{
    val optionalCustomer = customerRepository.findById(id)
    return if (optionalCustomer.isPresent) ResponseEntity.ok(optionalCustomer.get())
    else ResponseEntity.notFound().build()
  }
}