package com.rab3tech.customer.dao.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.Transaction;

/**
 * 
 * @author nagendra
 * 
 * Spring Data JPA repository
 *
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	@Query("Select t from Transaction t where t.creditAccountNumber=:pcreditAccountNumber or t.debitAccountNumber=:pdebitAccountNumber")
	List<Transaction> getAccountStatementData(@Param ("pcreditAccountNumber") String creditAccountNumber ,@Param("pdebitAccountNumber") String debitAccountNumber);
}

