package com.incomm.cclp.account.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CardEntityTest {
	
	CardEntity cardEntity;
	
	//@BeforeClass
	public void init() {
		cardEntity = CardEntity.builder().cardNumberHash("KPootnxEAy3zd3TTJIdGxud9wLI6SA0Z0c2L4C108Es=")
				.cardStatus(CardStatusType.ON_HOLD)
				.activationDate(LocalDateTime.now())
				.firstTimeTopUp(true).build();			
	}
	
	@Test
	public void testUpdateCardStatus() {
		init();
		CardStatusType cardStatusType = CardStatusType.CLOSED;
		CardUpdate update = cardEntity.updateCardStatus(cardStatusType);
		assertThat(update.getNewCardStatus(), is(CardStatusType.CLOSED));
		assertThat(update.getOldCardStatus(), is(CardStatusType.ON_HOLD));
		
	}
	
	@Test
	public void updateCardStatusToActive() {
		init();
		CardUpdate update = cardEntity.updateCardStatusToActive(true);
		assertThat(update.getNewCardStatus(), is(CardStatusType.ACTIVE));
		assertThat(update.getOldCardStatus(), is(CardStatusType.ON_HOLD));
		
	}
}
