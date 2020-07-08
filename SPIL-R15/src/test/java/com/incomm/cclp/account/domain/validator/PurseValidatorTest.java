package com.incomm.cclp.account.domain.validator;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;
import com.incomm.cclp.cache.AppCacheService;
import com.incomm.cclp.constants.ValueObjectKeys;

@RunWith(MockitoJUnitRunner.class)
public class PurseValidatorTest {

	@Mock
	private AppCacheService appCacheService;

	@InjectMocks
	private PurseValidator purseValidator;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private Long productId = 100L;

	private Long defaultPurseId = 2L;
	private Long purseId = 22L;

	private Map<String, Object> purseAttributes = Collections.singletonMap(ValueObjectKeys.MAX_PURSE_BALANCE, "300.00");

	@Before
	public void before() {
		// mock max card balance
		when(appCacheService.getProductAttribute(eq(ValueObjectKeys.GENERAL), eq(ValueObjectKeys.MAXCARDBAL))).thenReturn("200.00");

		// mock default purse id
		when(appCacheService.getProductAttribute(eq(ValueObjectKeys.PRODUCT), eq(ValueObjectKeys.DEFAULT_PURSE)))
			.thenReturn(defaultPurseId);

		// mock non-default purse attributes
		when(appCacheService.getProductAttributesForGroup(ValueObjectKeys.PURSE)).thenReturn(purseAttributes);

	}

	@Test
	public void testValidateMinAndMaxPurseBalance_validParameters_succeeds() throws Exception {

		purseValidator.validateMinAndMaxPurseBalance(appCacheService, productId, defaultPurseId, PurseUpdateActionType.LOAD, 50.00, 100.00);

		purseValidator.validateMinAndMaxPurseBalance(appCacheService, productId, defaultPurseId, PurseUpdateActionType.LOAD, 49.99, 150.01);

		purseValidator.validateMinAndMaxPurseBalance(appCacheService, productId, purseId, PurseUpdateActionType.LOAD, 50, 250);

		purseValidator.validateMinAndMaxPurseBalance(appCacheService, productId, purseId, PurseUpdateActionType.LOAD, 50.01, 249.99);

	}

	@Test
	public void testValidateMinAndMaxPurseBalance_exceedsDefaultPurseMaxBal_fails() throws Exception {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Unable to update purse balance to more than max purse balance.");

		purseValidator.validateMinAndMaxPurseBalance(appCacheService, productId, defaultPurseId, PurseUpdateActionType.LOAD, 50.01, 150.00);
	}

	@Test
	public void testValidateMinAndMaxPurseBalance_exceedsNonDefaultPurseMaxBal_fails() throws Exception {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Unable to update purse balance to negative value.");

		purseValidator.validateMinAndMaxPurseBalance(appCacheService, productId, defaultPurseId, PurseUpdateActionType.UNLOAD, 50.01,
				250.00);
	}

	@Test
	public void testValidateMinAndMaxPurseBalance_negativeBal_fails() throws Exception {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Unable to update purse balance to negative value.");

		purseValidator.validateMinAndMaxPurseBalance(appCacheService, productId, defaultPurseId, PurseUpdateActionType.UNLOAD, 50.00,
				50.01);
	}

	@Test
	public void testGetMaxPurseBalance() throws Exception {

		double maxPurseBalance = purseValidator.getMaxPurseBalance(appCacheService, productId, defaultPurseId);
		assertTrue("max Balance did not match for default purse", maxPurseBalance == 200.00);

		maxPurseBalance = purseValidator.getMaxPurseBalance(appCacheService, 100L, 22L);
		assertTrue("max Balance did not match for non-default purse", maxPurseBalance == 300.00);

	}

	@Test
	public void testIsDefaultPurseId() throws Exception {

		boolean isDefaultPurse = purseValidator.isDefaultPurseId(appCacheService, productId, defaultPurseId);
		assertTrue("Unable to get default purse.", isDefaultPurse);

		isDefaultPurse = purseValidator.isDefaultPurseId(appCacheService, productId, purseId);
		assertTrue("Unable to get non-default purse.", !isDefaultPurse);
	}

}
