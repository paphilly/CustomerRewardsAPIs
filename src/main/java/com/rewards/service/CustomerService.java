package com.rewards.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rewards.entity.Customer;
import com.rewards.entity.CustomerTransaction;
import com.rewards.exception.NoDataException;
import com.rewards.model.RewardPointsModel;
import com.rewards.model.TransactionModel;
import com.rewards.repository.CustomerRepository;
import com.rewards.repository.CustomerTransactionRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerTransactionRepository customerTransactionRepository;

	public RewardPointsModel getRewardsForCustomerForMonths(Long customerId, String months) {

		LocalDate dateRange = LocalDate.now().minusMonths(Long.parseLong(months));
		List<CustomerTransaction> transactionsList = customerTransactionRepository
				.findAllTransactionsFromDateForCustomer(dateRange, customerId).orElseThrow(() -> new NoDataException(
						"No transactions found for CustomerId " + String.valueOf(customerId)));
		Integer rewardPoints = countRewardPointsForCustomerTransactions(transactionsList);
		return new RewardPointsModel(customerId, rewardPoints);
	}

	public RewardPointsModel getRewardsForAllCustomersForMonths(String months) {

		LocalDate dateRange = LocalDate.now().minusMonths(Long.parseLong(months));
		List<CustomerTransaction> transactionsList = customerTransactionRepository.findAllTransactionsFromDate(dateRange)
				.orElseThrow(() -> new NoDataException("No transaction found for " + months + "months."));
		;
		return countRewardPointsForAllCustomers(transactionsList);
	}

	public Integer countRewardPointsForCustomerTransactions(List<CustomerTransaction> customerRewardList) {

		Integer rewardPoints = 0;
		List<Double> transactionAmounts = customerRewardList.stream().map(CustomerTransaction::getTransactionAmount)
				.collect(Collectors.toList());
		for (Double amount : transactionAmounts) {
			rewardPoints += calculateRewards(amount);
		}
		return rewardPoints;
	}

	public RewardPointsModel countRewardPointsForAllCustomers(List<CustomerTransaction> customerRewardList) {

		Integer totalRewardPoints = 0;
		RewardPointsModel customerRewardPoints = new RewardPointsModel();

		List<List<CustomerTransaction>> allCustomertransactions = customerRewardList.stream()
				.collect(Collectors.groupingBy(CustomerTransaction::getCustomer)).values().stream()
				.collect(Collectors.toList());

		for (List<CustomerTransaction> customerTransactions : allCustomertransactions) {
			for (CustomerTransaction transaction : customerTransactions) {

				totalRewardPoints += countRewardPointsForCustomerTransactions(customerTransactions);
				break;
			}
		}
		customerRewardPoints.setRewardPoints(totalRewardPoints);
		return customerRewardPoints;
	}

	int calculateRewards(Double transactionAmount) {

		int rewards = 0;
		if (transactionAmount > 100) {
			rewards += 50; // 50 points for first 100 on transactions above 100
			rewards += ((transactionAmount.intValue() - 100) * 2); // 2 points for transaction above 100
		}
		else if (transactionAmount.intValue() < 100 && transactionAmount.intValue() > 50) {
			rewards += transactionAmount.intValue() - 50; // 1 points for amount between 100 and 50.
			
		}
		return rewards;
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id).get();
	}

	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	public Customer updateCustomer(Long id, Customer newCustomer) {

		Customer customer = getCustomerById(id);
		customer.setFirstName(newCustomer.getFirstName());
		customer.setLastName(newCustomer.getLastName());
		customer.setRewards(newCustomer.getRewards());
		return customerRepository.save(customer);
	}

	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}

	public TransactionModel addTransaction(Long customerId, TransactionModel customerRewardModel) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new NoDataException("No data found with CustomerId " + String.valueOf(customerId)));

		CustomerTransaction customerTransaction = mapToCustomerRewardEntity(customerRewardModel);
		customerTransaction.setRewardPoints(calculateRewards(customerRewardModel.getTransactionAmount()));
		customerTransaction.setCustomer(customer);
		customer.getRewards().add(customerTransaction);
		customerRepository.save(customer);
		CustomerTransaction reward = customer.getRewards().stream()
				.filter(rewards -> (customerTransaction.getTransactionDate().equals(rewards.getTransactionDate()))
						&& customerTransaction.getRewardPoints().equals(rewards.getRewardPoints()))
				.findAny().orElse(null);

		return mapToCustomerRewardModel(reward);

	}

	private CustomerTransaction mapToCustomerRewardEntity(TransactionModel rewardModel) {
		CustomerTransaction reward = new CustomerTransaction();
		reward.setRewardPoints(rewardModel.getRewardPoints());
		reward.setTransactionDate(rewardModel.getTransactionDate());
		reward.setTransactionAmount(rewardModel.getTransactionAmount());
		return reward;
	}

	private TransactionModel mapToCustomerRewardModel(CustomerTransaction customerTransaction) {
		TransactionModel rewardModel = new TransactionModel();
		rewardModel.setRewardId(customerTransaction.getRewardId());
		rewardModel.setRewardPoints(customerTransaction.getRewardPoints());
		rewardModel.setTransactionDate(customerTransaction.getTransactionDate());
		rewardModel.setTransactionAmount(customerTransaction.getTransactionAmount());
		return rewardModel;
	}

}
