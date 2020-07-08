package com.incomm.cclp.account.domain.event.publisher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.incomm.cclp.account.domain.event.subscriber.DomainEventSubscriber;

public class AccountDomainEventPublisherTest {

	@Test
	public void testAddSubscriber() throws Exception {

		AccountDomainEventPublisher publisher = this.getPublisher();
		DomainEventSubscriber subscriber = this.getSubscriber();

		publisher.addSubscriber(subscriber);
		MatcherAssert.assertThat("Unable to add event subscriber", publisher.getSubscribers()
			.contains(subscriber), is(equalTo(true)));

	}

	@Test
	public void testRemoveSubscriber() throws Exception {

		AccountDomainEventPublisher publisher = this.getPublisher();
		DomainEventSubscriber subscriber = this.getSubscriber();

		publisher.addSubscriber(subscriber);

		publisher.removeSubscriber(subscriber);

		MatcherAssert.assertThat("Unable to remove event subscriber", publisher.getSubscribers()
			.isEmpty(), is(equalTo(true)));
	}

	private AccountDomainEventPublisher getPublisher() {
		return new AccountDomainEventPublisher();
	}

	private DomainEventSubscriber getSubscriber() {
		return new TestDomainEventSubscriber();
	}

}
