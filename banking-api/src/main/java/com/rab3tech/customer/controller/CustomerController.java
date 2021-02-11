package com.rab3tech.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.PayeeInfoVO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerController {
	
	@Autowired
	private PayeeInfoService payeeInfoService; 
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/customer/findPayee/{payeeId}")
	public PayeeInfoVO findPayee(@PathVariable int payeeId) {
		PayeeInfoVO payeeVo = new PayeeInfoVO();
		payeeVo = payeeInfoService.findPayeeById(payeeId);
		return payeeVo;
	}
	
	@GetMapping("/customer/deletePayee/{payeeId}") //http://localhost:999/v3/customer/deletePayee/1
	public ApplicationResponseVO deletePayee(@PathVariable int payeeId) {
		ApplicationResponseVO payeeVo = new ApplicationResponseVO();
		 payeeInfoService.deletePayeeById(payeeId);
		 payeeVo.setCode(200);
		 payeeVo.setMessage("Payee has been deleted sucessfully");
		 payeeVo.setStatus("Success");
		return payeeVo;
	}
	
	@PostMapping("/customer/editPayee") //http://localhost:999/v3/customer/editPayee/
	public ApplicationResponseVO editPayee(@RequestBody PayeeInfoVO payeeInfoVO) {
		ApplicationResponseVO payeeVo = new ApplicationResponseVO();
		 payeeInfoService.editPayee(payeeInfoVO);
		 payeeVo.setCode(200);
		 payeeVo.setMessage("Payee has been updated sucessfully");
		 payeeVo.setStatus("Success");
		 
		//Send email after success 
		 PayeeInfoVO payee = findPayee(payeeInfoVO.getId());
		 EmailVO email = new EmailVO(payee.getCustomerId(), "cubicbatch.3@gmail.com", "Edit Payee email ", payee.getPayeeAccountNo(), payee.getPayeeName());
		 emailService.sendEditPayeeEMail(email);
		 
		return payeeVo;
	}

}
