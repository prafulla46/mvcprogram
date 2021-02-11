package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.PayeeInfo;

public interface PayeeInfoRepository extends JpaRepository<PayeeInfo, Integer> {
	Optional<PayeeInfo> findByCustomerIdAndPayeeAccountNo(String customerId, String accountNumber);
	List<PayeeInfo> findByCustomerId(String customerId);
	 Optional<PayeeInfo> findById(Integer id);
}
