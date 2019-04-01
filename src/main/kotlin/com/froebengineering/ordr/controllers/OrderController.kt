package com.froebengineering.ordr.controllers

import com.froebengineering.ordr.BLOCK_NOTE_PARAM
import com.froebengineering.ordr.ERASER_PARAM
import com.froebengineering.ordr.PAPER_PARAM
import com.froebengineering.ordr.PEN_PARAM
import com.froebengineering.ordr.domain.Item
import com.froebengineering.ordr.domain.Order
import com.froebengineering.ordr.repository.CustomerRepository
import com.froebengineering.ordr.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Suppress("unused")
@RestController
@RequestMapping(value = ["/order"])
class OrderController @Autowired constructor(
    val orderRepository: OrderRepository,
    val customerRepository: CustomerRepository
) {
  
  @PostMapping("/{customerId}")
  fun create(@PathVariable customerId: Long): ResponseEntity<Order> {
    val optionalCustomer = customerRepository.findById(customerId)
    
    return if (optionalCustomer.isPresent) {
      val order = orderRepository.save(Order(customerType = optionalCustomer.get().type))
      ResponseEntity.ok(order)
    } else
      ResponseEntity.notFound().build()
  }
  
  // Would have used Spring security with more time
  @PatchMapping("/{id}")
  fun update(
      @PathVariable id: Long,
      @RequestParam(value = PEN_PARAM, required = false) numberOfPens: Int? = 0,
      @RequestParam(value = BLOCK_NOTE_PARAM, required = false) numberOfBlockNotes: Int? = 0,
      @RequestParam(value = PAPER_PARAM, required = false) numberOfPapers: Int? = 0,
      @RequestParam(value = ERASER_PARAM, required = false) numberOfErasers: Int? = 0): ResponseEntity<Order> {
    
    val order: Order
    
    orderRepository.findById(id).apply {
      if (isPresent) {
        order = get().apply {
          numberOfPens?.let { updateRow(Item.PEN, it) }
          numberOfBlockNotes?.let { updateRow(Item.BLOCK_NOTES, it) }
          numberOfPapers?.let { updateRow(Item.PAPER, it) }
          numberOfErasers?.let { updateRow(Item.ERASER, it) }
        }
      } else return ResponseEntity.notFound().build()
    }
    return ResponseEntity.ok(
        orderRepository.save(order))
  }
  
  // Also confirm user with Spring security
  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): ResponseEntity<Order> {
    
    val optionalOrder = orderRepository.findById(id)
    
    return if (optionalOrder.isPresent) ResponseEntity.ok(optionalOrder.get())
    else ResponseEntity.notFound().build()
  }
}