package com.froebengineering.ordr.domain

import javax.persistence.*


enum class CustomerType {
  SMALL,
  LARGE,
  PRIVATE
}

@Entity
data class Customer(
    @Column(name = "name")
    val name: String,
    
    @Column(name = "customer_type")
    val type: CustomerType,
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val orders: MutableList<Order> = mutableListOf()
)
