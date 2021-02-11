package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;

@Service
@Transactional
public class PayeeInfoServiceImpl implements PayeeInfoService{

	@Autowired
	PayeeInfoRepository payeeInfoRepository;
	@Autowired
	CustomerAccountInfoRepository customerAccountInfoRepository;
	
	@Autowired
	MagicCustomerRepository magicCustomerRepository;
	@Override
	public String savePayee(PayeeInfoVO payeeInfoVO) {
		//logged in user has account or not
		String message = null;
		Optional<CustomerAccountInfo>  customerAccount = customerAccountInfoRepository.findByCustomerId(payeeInfoVO.getCustomerId());
		if(customerAccount.isPresent()) {
			//logged in user has saving account 
			if(!customerAccount.get().getAccountType().equals("SAVING")) {
				message = "You do not have valid saving account so you cannot add Payee";
				return message;
			}
		}else {
			message = "You do not have valid account so you cannot add Payee";
			return message;
		}
		//payee account number is valid or not 
		Optional<CustomerAccountInfo>  payeeAccount = customerAccountInfoRepository.findByAccountNumber(payeeInfoVO.getPayeeAccountNo());
		if(payeeAccount.isEmpty()) {
			message = "Payee acoount number is not valid so you cannot add Payee";
			return message;
		}
		//user's account number shud not be same as payee account no 
		
		if(customerAccount.get().getAccountNumber().equals(payeeAccount.get().getAccountNumber())) {
			message = "Payee acoount number cannot be same as user's account number";
			return message;
		}
		//4->Check payee name from db   PLEASE IMPLEMENT
		
		 Optional<Customer>  customer = magicCustomerRepository.findByEmail(payeeAccount.get().getCustomerId());
		
		 if(! customer.get().getName().equalsIgnoreCase(payeeInfoVO.getPayeeName())) {//Sahara SAHARA
			 message = "Payee Name is not correct ";
			 return message; 
		 }
		 //cannot add same payee again 
		 Optional<PayeeInfo> payeeAcc = payeeInfoRepository.findByCustomerIdAndPayeeAccountNo(payeeInfoVO.getCustomerId(), payeeInfoVO.getPayeeAccountNo());
		 if(payeeAcc.isPresent()) {
			 message = "Same payee cannot be added again";
			 return message; 
		 }
		PayeeInfo payee = new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, payee);
		payee.setDoe(new Timestamp(new Date().getTime()));
		payee.setDom(new Timestamp(new Date().getTime()));
		payee.setUrn(1);
		payeeInfoRepository.save(payee);
		message = "Payee has been added successfully";
		return message;
	}
	
	@Override
    public List<PayeeInfoVO> findByCustomerId(String username) {
        List<PayeeInfoVO> payees = new ArrayList<PayeeInfoVO>();
        List<PayeeInfo> payeeInfo = payeeInfoRepository.findByCustomerId(username);
        for (PayeeInfo payeelist : payeeInfo) {
            PayeeInfoVO payeeinfoVo = new PayeeInfoVO();
            BeanUtils.copyProperties(payeelist, payeeinfoVo);
            payees.add(payeeinfoVo);
        }
        return payees;
	}

	@Override
	public PayeeInfoVO findPayeeById(int payeeId) {
		 PayeeInfoVO payeeinfoVo = new PayeeInfoVO();
		 Optional<PayeeInfo> payee =   payeeInfoRepository.findById(payeeId);
		 if(payee.isPresent()) {
			 BeanUtils.copyProperties(payee.get(), payeeinfoVo);
		 }
		return payeeinfoVo;
	}

	@Override
	public void deletePayeeById(int payeeId) {
		payeeInfoRepository.deleteById(payeeId);
	}

	@Override
	public void editPayee(PayeeInfoVO payeeInfoVO) {
		PayeeInfoVO payeeInDB = findPayeeById(payeeInfoVO.getId());
		payeeInDB.setPayeeAccountNo(payeeInfoVO.getPayeeAccountNo());
		payeeInDB.setPayeeName(payeeInfoVO.getPayeeName());
		payeeInDB.setPayeeNickName(payeeInfoVO.getPayeeNickName());
		payeeInDB.setRemarks(payeeInfoVO.getRemarks());
		payeeInDB.setDom(new Timestamp(new Date().getTime()));
		PayeeInfo payee = new PayeeInfo();
		BeanUtils.copyProperties(payeeInDB, payee);
		payeeInfoRepository.save(payee);
	}
}
