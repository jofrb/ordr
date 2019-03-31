package com.froebengineering.ordr.repository

import com.froebengineering.ordr.domain.OrderRow
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Suppress("unused")
@Repository
interface ItemRepository: CrudRepository<OrderRow, Long>