package com.rewards.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.entity.Customer;
import com.rewards.model.RewardPointsModel;
import com.rewards.model.TransactionModel;
import com.rewards.service.CustomerService;

import jakarta.ws.rs.PathParam;

@RestController
@RequestMapping("")
public class CustomerRewardsController {

	@Autowired
	private CustomerService customerService;
	
	// Primary assesment task

	@GetMapping("/rewards/{customerId}")
	ResponseEntity<RewardPointsModel> getRewardPointsForCustomerByMonths(@PathVariable("customerId") Long customerId,
			@RequestParam("months") String months) {

		RewardPointsModel rewardPoints = customerService.getRewardsForCustomerForMonths(customerId, months);
		return ResponseEntity.status(HttpStatus.OK).body(rewardPoints);
	}

	@GetMapping("/rewards")
	ResponseEntity<RewardPointsModel> getRewardPointsForAllCustomerByMonths(
			@RequestParam("months") String months) {

		RewardPointsModel rewardPointsByCustomer = customerService.getRewardsForAllCustomersForMonths(months);
		return ResponseEntity.status(HttpStatus.OK).body(rewardPointsByCustomer);
	}
	
	// Miscellaneous additions

	@GetMapping("/customers")
	ResponseEntity<List<Customer>> getCustomers() {

		List<Customer> customers = customerService.getAllCustomers();
		return ResponseEntity.status(HttpStatus.OK).body(customers);
	}

	@GetMapping("/customers/{customerId}")
	ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {

		Customer customer = customerService.getCustomerById(customerId);
		return ResponseEntity.status(HttpStatus.OK).body(customer);

	}

	@PostMapping("/customers/{customerId}/transactions")
	ResponseEntity<TransactionModel> addTransaction(@PathVariable Long customerId,
			@RequestBody TransactionModel customerRewardModel) {

		customerRewardModel = customerService.addTransaction(customerId, customerRewardModel);
		return ResponseEntity.status(HttpStatus.OK).body(customerRewardModel);

	}
	
	// Create customer can be added

}
