package com.froebengineering.ordr.domain

import javax.persistence.*

const val FREE_BIKE_LIMIT: Float = 10000F

@Entity
@Table(name = "order_table")
data class Order(
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    
    @Column(name = "customer_type")
    var customerType: CustomerType,
    
    // Would rather find out if map is possible to implement
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val rows: MutableList<OrderRow> = mutableListOf(),
    
    @Column(name = "price_sum")
    var priceSum: Float = 0F,
    
    @Column(name = "discount_sum")
    var discountSum: Float = 0F

) {
  
  fun updateRow(item: Item, number: Int) {
    
    this.rows.firstOrNull { item == it.item }?.let {
      if (number < 1) removeRow(it) else {
        updateNumberOfItemsInRow(it, number)
      }
    } ?: if (number > 0) addRow(item, number)
  }
  
  private fun addRow(item: Item, number: Int) {
    rows.add(OrderRow(item = item, numberOfItems = number).apply { refreshLine(customerType) })
    refreshOrder()
  }
  
  private fun removeRow(it: OrderRow) {
    this.rows.remove(it)
    refreshOrder()
  }
  
  private fun updateNumberOfItemsInRow(it: OrderRow, number: Int) {
    it.updateNumberOfItems(number, customerType)
    refreshOrder()
  }
  
  private fun refreshOrder() {
    refreshPrice()
    refreshDiscount()
    updateFreeBike()
  }
  
  private fun refreshPrice() {
    priceSum = rows.map { it.priceSum }.reduce { acc, fl -> acc + fl }
  }
  
  private fun refreshDiscount() {
    discountSum = if (customerType != CustomerType.PRIVATE)
      rows.map { it.discountSum }.reduce { acc, fl -> acc + fl } else 0F
  }
  
  private fun updateFreeBike() {
    if (priceSum - discountSum >= FREE_BIKE_LIMIT) {
      if (!rows.map { it.item }.contains(Item.BIKE)) rows.add(OrderRow(numberOfItems = 1, item = Item.BIKE)) // Only adds without updating
    } else rows.firstOrNull { it.item == Item.BIKE }?.let { this.rows.remove(it) } //Only removes without updating
  }
}
