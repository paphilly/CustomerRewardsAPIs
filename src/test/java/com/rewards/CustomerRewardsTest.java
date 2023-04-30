package com.rewards;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.rewards.controller.CustomerRewardsController;
import com.rewards.entity.Customer;
import com.rewards.entity.CustomerTransaction;
import com.rewards.model.RewardPointsModel;
import com.rewards.service.CustomerService;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(CustomerRewardsController.class)
public class CustomerRewardsTest {

	@MockBean
	private CustomerService customerService;

	@InjectMocks
	private CustomerRewardsController customerController;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
	}

	@Test
	public void testGetAllCustomers() throws Exception {
		List<Customer> customers = new ArrayList<>();
		customers.add(new Customer(1L, "John", "Doe"));
		customers.add(new Customer(2L, "Jane", "Smith"));

		Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

		mockMvc.perform(MockMvcRequestBuilders.get("/customers")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.equalTo("John")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.equalTo("Smith")));
	}

	@Test
	public void testGetCustomerById() throws Exception {
		Customer customer = new Customer(1L, "John", "Doe");

		Mockito.when(customerService.getCustomerById(1L)).thenReturn(customer);

		mockMvc.perform(MockMvcRequestBuilders.get("/customers/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.equalTo("John")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.equalTo("Doe")));
	}

	@Test
	public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
		Customer alex = new Customer(1L, "alex", "Jones");
		Customer alex1 = new Customer(2L, "alex1", "Jones");
		Customer alex2 = new Customer(3L, "alex2", "Jones");

		List<Customer> allEmployees = Arrays.asList(alex, alex1, alex2);

		given(customerService.getAllCustomers()).willReturn(allEmployees);

		mockMvc.perform(get("/customers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].firstName", is(alex.getFirstName())))
				.andExpect(jsonPath("$[1].firstName", is(alex1.getFirstName())))
				.andExpect(jsonPath("$[2].firstName", is(alex2.getFirstName())));
		verify(customerService, VerificationModeFactory.times(1)).getAllCustomers();
		reset(customerService);
	}

	@Test
	public void test_getRewardPointsForAllCustomerByMonths() throws Exception {
		Customer customer1 = new Customer(1L, "John", "Doe");

		CustomerTransaction customer1_transaction1 = new CustomerTransaction(Double.valueOf("250"), LocalDate.of(2023, 04, 28),
				1L);
		CustomerTransaction customer1_transaction2 = new CustomerTransaction(Double.valueOf("150"), LocalDate.of(2023, 04, 20),
				1L);
		customer1_transaction1.setCustomer(customer1);
		customer1_transaction2.setCustomer(customer1);

		Customer customer2 = new Customer(2L, "Jane", "Smith");
		CustomerTransaction customer2_transaction1 = new CustomerTransaction(Double.valueOf("110"), LocalDate.of(2023, 04, 20),
				2L);
		customer2_transaction1.setCustomer(customer2);

		List<CustomerTransaction> allCustomerTransactionList = new ArrayList<CustomerTransaction>();
		allCustomerTransactionList.add(customer1_transaction1);
		allCustomerTransactionList.add(customer1_transaction2);
		allCustomerTransactionList.add(customer2_transaction1);

		RewardPointsModel rewardPointsModel = new RewardPointsModel();
		rewardPointsModel.setRewardPoints(350);

		when(customerService.getRewardsForAllCustomersForMonths("3")).thenReturn(rewardPointsModel);

		mockMvc.perform(MockMvcRequestBuilders.get("/rewards?months=3"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.rewardPoints", Matchers.equalTo(350)));

	}

	@Test
	public void test_getRewardPointsForCustomerForMonths() throws Exception {
		Customer customer1 = new Customer(1L, "John", "Doe");

		CustomerTransaction customer1_transaction1 = new CustomerTransaction(Double.valueOf("250"), LocalDate.of(2023, 04, 28),
				1L);
		CustomerTransaction customer1_transaction2 = new CustomerTransaction(Double.valueOf("100"), LocalDate.of(2023, 04, 20),
				1L);
		customer1_transaction1.setCustomer(customer1);
		customer1_transaction2.setCustomer(customer1);

		RewardPointsModel rewardPointsModel = new RewardPointsModel();
		rewardPointsModel.setRewardPoints(350);

		when(customerService.getRewardsForCustomerForMonths(1L, "3")).thenReturn(rewardPointsModel);

		mockMvc.perform(MockMvcRequestBuilders.get("/rewards/1?months=3"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.rewardPoints", Matchers.equalTo(350)));

	}

}
