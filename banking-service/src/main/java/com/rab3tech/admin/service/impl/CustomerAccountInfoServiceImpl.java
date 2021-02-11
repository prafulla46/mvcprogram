package com.rab3tech.admin.service.impl;

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
import com.rab3tech.admin.service.CustomerAccountInfoService;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.Transaction;
import com.rab3tech.vo.AccountStatementVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.TransactionVO;

@Service
@Transactional
public class CustomerAccountInfoServiceImpl implements CustomerAccountInfoService{

	@Autowired
	private MagicCustomerRepository customerRepository;
	
	@Autowired
	CustomerAccountInfoRepository customerAccountInfoRepository; //new repository 
	
	@Autowired
	PayeeInfoRepository payeeInfoRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public List<CustomerVO> findCustomers() {
		List<CustomerVO> customerVos = new ArrayList<CustomerVO>();
		List<Customer> customers = customerRepository.findAll(); //find all the customers present in db
		for (Customer customer:customers) { //iterate through each customer
			//first check if this customer is present in account table 
			Optional<CustomerAccountInfo> accountInfo = customerAccountInfoRepository.findByCustomerId(customer.getEmail()); //check customer is in account table
			if(!accountInfo.isPresent()) {//check we got any row or not
				CustomerVO customervo = new CustomerVO();
				BeanUtils.copyProperties(customer, customervo);
				customerVos.add(customervo);
			}
		}
		return customerVos;
	}

	@Override
	public void createAccount(int customerId) {
		CustomerAccountInfo accountInfo = new CustomerAccountInfo();
		String accountNumber = "1000"+customerId; //10001
		accountInfo.setAccountNumber(accountNumber);
		accountInfo.setAccountType("SAVING");
		accountInfo.setAvBalance(1000);
		accountInfo.setBranch("Fremont");
		accountInfo.setCurrency("USD");
		//getting customer's email 
		Optional<Customer> customer = customerRepository.findById(customerId);
		accountInfo.setCustomerId(customer.get().getEmail());
		accountInfo.setStatusAsOf(new Date());
		accountInfo.setTavBalance(1000);
		System.out.println("accountInfo----"+accountInfo);
		customerAccountInfoRepository.save(accountInfo);
	}

	@Override
	public CustomerAccountInfoVO findCustomer(String customerId) {
		Optional<CustomerAccountInfo> account =  customerAccountInfoRepository.findByCustomerId(customerId);
		CustomerAccountInfoVO accountInfoVO = new CustomerAccountInfoVO();
		if(account.isPresent()) {
			BeanUtils.copyProperties(account.get(), accountInfoVO);
		}
		return accountInfoVO;
	}

	@Override
	public String saveTransaction(TransactionVO transactionVO) {  //Debit->giver Credit-Receive
		String message = null;
		//check debitor has valid account 
		Optional<CustomerAccountInfo> accountDebitor =  customerAccountInfoRepository.findByCustomerId(transactionVO.getCustomerID());
		if(accountDebitor.isPresent()){
			//creditor has enough balance 
			if(transactionVO.getAmount() > accountDebitor.get().getAvBalance()) {
				message = "Creditor is not having sufficient balance";
				return message;
			}
		}else {
			message = "Creditor is not having valid account ";
			return message;
		}
		//Check creditor has valid account 
		Optional<CustomerAccountInfo> accountCredtor =  customerAccountInfoRepository.findByAccountNumber(transactionVO.getCreditAccountNumber());
		if(accountCredtor.isEmpty()){
			message = "Debitor is not having valid account ";
			return message;
		}
		//Debitor is available in creditors payee list 
		Optional<PayeeInfo>  payee = payeeInfoRepository.findByCustomerIdAndPayeeAccountNo(transactionVO.getCustomerID(), transactionVO.getCreditAccountNumber());
		if(payee.isEmpty()) {
			message = "Debitor is not valid for Creditor ";
			return message;
		}
		//update debitor's balance
		float debitAmount = accountDebitor.get().getAvBalance() - transactionVO.getAmount();
		accountDebitor.get().setAvBalance(debitAmount);
		accountDebitor.get().setTavBalance(debitAmount);
		accountDebitor.get().setStatusAsOf(new Date());
		customerAccountInfoRepository.save(accountDebitor.get());
		//update creditor's balance 
		float creditAmount = accountCredtor.get().getAvBalance() + transactionVO.getAmount();
		accountCredtor.get().setAvBalance(creditAmount);
		accountCredtor.get().setTavBalance(creditAmount);
		accountCredtor.get().setStatusAsOf(new Date());
		customerAccountInfoRepository.save(accountCredtor.get());
		//update transaction table 
		Transaction transaction = new Transaction();
		BeanUtils.copyProperties(transactionVO, transaction);
		transaction.setDebitAccountNumber(accountDebitor.get().getAccountNumber());
		transaction.setTransactionDate(new Timestamp(new Date().getTime()));
		transactionRepository.save(transaction);
		message = "Amount transfer has been done sucessfully";
		return message;
	}

	@Override
	public List<AccountStatementVO> getStatement(String email) {
		 List<AccountStatementVO> statements = new ArrayList<AccountStatementVO>();
		//get user's account number 
		Optional<CustomerAccountInfo> account =  customerAccountInfoRepository.findByCustomerId(email);
		List<Transaction> transactions = transactionRepository.getAccountStatementData(account.get().getAccountNumber(),account.get().getAccountNumber());
		// trx id , account no , date , debit/credit , amount , date 
		
		//debit -> means - amount from my account  
		//Credit -> amount received 
		for(Transaction transaction : transactions) {
			AccountStatementVO statement = new AccountStatementVO();
			statement.setTransactionId(transaction.getId());
			statement.setAmount(transaction.getAmount());
			statement.setRemarks(transaction.getRemark());
			statement.setDate(transaction.getTransactionDate());
			if(account.get().getAccountNumber().equals(transaction.getCreditAccountNumber())) {
				statement.setTransactionType("Credit");
				statement.setAccountNo(transaction.getDebitAccountNumber());
			}else {
				//debit
				statement.setTransactionType("Debit");
				statement.setAccountNo(transaction.getCreditAccountNumber());
			}
			statements.add(statement);
		}
		
		return statements;
	}

}
