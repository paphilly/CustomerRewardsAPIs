package com.rewards.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SuppressWarnings("unused")
public class RewardPointsModel {

	@JsonInclude(Include.NON_NULL)
	private Long customerId;
	private Integer rewardPoints;

	public RewardPointsModel(Long customerId, Integer rewardPoints) {
		this.customerId = customerId;
		this.rewardPoints = rewardPoints;
	}

	public RewardPointsModel() {
	}
}
