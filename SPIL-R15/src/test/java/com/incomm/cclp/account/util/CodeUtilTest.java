package com.incomm.cclp.account.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class CodeUtilTest {

	public void trimToLength() {

	}

	@Test
	public void test_isPositive() {

		assertThat(CodeUtil.isPositive(BigDecimal.ONE)).isTrue();

		assertThat(CodeUtil.isPositive(BigDecimal.ZERO)).isFalse();

		assertThat(CodeUtil.isPositive(new BigDecimal("-1"))).isFalse();
	}

	@Test
	public void test_isPositiveOrZero() {
		assertThat(CodeUtil.isZeroOrPositive(BigDecimal.ONE)).isTrue();

		assertThat(CodeUtil.isZeroOrPositive(new BigDecimal("0.01"))).isTrue();
		assertThat(CodeUtil.isZeroOrPositive(BigDecimal.ZERO)).isTrue();

		assertThat(CodeUtil.isZeroOrPositive(new BigDecimal("-0.01"))).isFalse();
		assertThat(CodeUtil.isZeroOrPositive(new BigDecimal("-1"))).isFalse();
	}

	@Test
	public void test_isZero() {
		assertThat(CodeUtil.isZero(BigDecimal.ONE)).isFalse();

		assertThat(CodeUtil.isZero(BigDecimal.ZERO)).isTrue();

		assertThat(CodeUtil.isZero(new BigDecimal("0.01"))).isFalse();
		assertThat(CodeUtil.isZero(new BigDecimal("-0.01"))).isFalse();
		assertThat(CodeUtil.isZero(new BigDecimal("-1"))).isFalse();
	}

	@Test
	public void test_isEmpty() {

		assertThat(CodeUtil.isEmpty(null)).isTrue();
		assertThat(CodeUtil.isEmpty(Collections.emptyList())).isTrue();

		assertThat(CodeUtil.isEmpty(Arrays.asList("A"))).isFalse();
	}

	@Test
	public void test_isNotEmpty() {

		assertThat(CodeUtil.isNotEmpty(null)).isFalse();
		assertThat(CodeUtil.isNotEmpty(Collections.emptyList())).isFalse();

		assertThat(CodeUtil.isNotEmpty(Arrays.asList("A"))).isTrue();
	}

	@Test
	public void test_hasOneElementOnly() {

		assertThat(CodeUtil.hasOneElementOnly(null)).isFalse();
		assertThat(CodeUtil.hasOneElementOnly(Collections.emptyList())).isFalse();

		assertThat(CodeUtil.hasOneElementOnly(Arrays.asList("A"))).isTrue();

		assertThat(CodeUtil.hasOneElementOnly(Arrays.asList("A", "B"))).isFalse();

	}

}
