package com.froebengineering.ordr.repository

import com.froebengineering.ordr.domain.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: CrudRepository<Order, Long>