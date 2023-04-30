package com.rewards.model;

import java.time.LocalDate;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SuppressWarnings("unused")
public class TransactionModel {
	
	private Long rewardId;

	private Double transactionAmount;

	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate transactionDate;

	private Integer rewardPoints;

	private Long customerid;

}
