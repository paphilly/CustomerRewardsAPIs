package com.rewards.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.rewards.entity.CustomerTransaction;

import lombok.Data;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SuppressWarnings("unused")
public class CustomerModel {

	private Long customer_id;

	private String firstName;

	private String lastName;

	private List<TransactionModel> transactions;
}
