package com.rewards.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;

//import org.hibernate.annotations.Table;

//import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CustomerTransactions")
public class CustomerTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactionId")
	private Long transactionId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "customerid", nullable = false)
	@JsonBackReference
	private Customer customer;

	@Column(name = "TransactionAmount")
	private Double transactionAmount;

	@Column(name = "TransactionDate")
	private LocalDate transactionDate;

	@Column(name = "RewardPoints")
	private Integer rewardPoints;

	public Long getRewardId() {
		return transactionId;
	}

	public void setRewardId(Long rewardId) {
		this.transactionId = rewardId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Integer getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Integer rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public CustomerTransaction(Long rewardId, Double transactionAmount, LocalDate transactionDate) {
		super();
		this.transactionId = rewardId;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
	}

	public CustomerTransaction(Double transactionAmount, LocalDate transactionDate, Long customerId) {
		super();
		this.transactionId = transactionId;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
		//this.customer.setCustomerid(customerId);
	}

	public CustomerTransaction() {
		super();
	}
}
