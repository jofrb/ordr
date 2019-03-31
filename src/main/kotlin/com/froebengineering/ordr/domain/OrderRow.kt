package com.froebengineering.ordr.domain

import javax.persistence.*

const val CORPORATE_DISCOUNT = 0.1F
const val LARGE_CORPORATE_DISCOUNT = 0.3F

enum class Item(val price: Float) {
  PEN(3.5F),
  BLOCK_NOTES(32F),
  PAPER(120F),
  ERASER(30F),
  BIKE(0F)
  
}

@Entity
data class OrderRow(
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    
    val item: Item,
    
    @Column(name = "number_of_items")
    var numberOfItems: Int,
    
    var priceSum: Float = 0F,
    
    var discountSum: Float = 0F

) {
  
  fun updateNumberOfItems(numberOfItems: Int, type: CustomerType){
    this.numberOfItems = numberOfItems
    refreshLine(type)
  }
  
  private fun refreshLine(customerType: CustomerType) {
    calculatePrice()
    calculateDiscount(customerType)
  }
  
  private fun calculatePrice() {
    priceSum = item.price * numberOfItems
  }
  
  private fun calculateDiscount(customerType: CustomerType) {
    // Could have done is not but don't know if more categories will be added in future
    discountSum = when (customerType) {
      CustomerType.LARGE ->
        priceSum * if (item == Item.PAPER || item == Item.PAPER) LARGE_CORPORATE_DISCOUNT else CORPORATE_DISCOUNT
      CustomerType.SMALL -> priceSum * CORPORATE_DISCOUNT
      else -> 0F
    }
  }
}