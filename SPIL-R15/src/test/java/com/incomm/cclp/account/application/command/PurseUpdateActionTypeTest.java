package com.incomm.cclp.account.application.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.Test;

import com.incomm.cclp.account.domain.model.PurseUpdateActionType;

public class PurseUpdateActionTypeTest {

	@Test
	public void testPurseLoadActionType() {
		Optional<PurseUpdateActionType> actionType;

		actionType = PurseUpdateActionType.byAction("load");
		assertThat(actionType.isPresent(), is(true));
		assertThat(actionType.get(), is(PurseUpdateActionType.LOAD));

		actionType = PurseUpdateActionType.byAction("unload");
		assertThat(actionType.isPresent(), is(true));
		assertThat(actionType.get(), is(PurseUpdateActionType.UNLOAD));

		actionType = PurseUpdateActionType.byAction("dummy");
		assertThat(actionType.isPresent(), is(false));

	}

}
