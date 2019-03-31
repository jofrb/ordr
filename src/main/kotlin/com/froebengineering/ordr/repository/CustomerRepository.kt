package com.froebengineering.ordr.repository

import com.froebengineering.ordr.domain.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: CrudRepository<Customer, Long>