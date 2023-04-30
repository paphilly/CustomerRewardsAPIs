package com.rewards.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rewards.entity.CustomerTransaction;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Long> {

	@Query("SELECT r FROM CustomerTransaction r WHERE r.transactionDate >= :dateRange")
	Optional<List<CustomerTransaction>> findAllTransactionsFromDate(LocalDate dateRange);

	@Query("SELECT r FROM CustomerTransaction r WHERE r.transactionDate >= :dateRange AND r.customer.customerid = :customerId")
	Optional<List<CustomerTransaction>> findAllTransactionsFromDateForCustomer(LocalDate dateRange, Long customerId );

}
