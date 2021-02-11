package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.PayeeInfoVO;

public interface PayeeInfoService {

	public String savePayee(PayeeInfoVO payeeInfoVO);
	List <PayeeInfoVO> findByCustomerId(String customerId);
	PayeeInfoVO findPayeeById(int payeeId);
	void deletePayeeById(int payeeId);
	public void editPayee(PayeeInfoVO payeeInfoVO);
}
