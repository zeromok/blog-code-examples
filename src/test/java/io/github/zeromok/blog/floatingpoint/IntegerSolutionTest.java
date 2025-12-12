package io.github.zeromok.blog.floatingpoint;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@DisplayName("정수 연산을 통한 부동소수점 해결 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegerSolutionTest {
	@Test
	@Order(1)
	@DisplayName("double 계산은 부정확하다")
	void doubleCalculation_inaccurate() {
		// given
		double price = 19.95;
		double tax = price * 0.1;

		// then
		BigDecimal actual = new BigDecimal(tax);
		assertNotEquals(new BigDecimal("1.995"), actual);
		// 실제로는 1.9949999... 형태로 저장됨
		String actualStr = actual.toString();
		assertTrue(actualStr.startsWith("1.994") || actualStr.startsWith("1.995"));
	}

	@Test
	@Order(2)
	@DisplayName("정수 계산은 정확하다")
	void integerCalculation_accurate() {
		// given
		long priceInCents = 1995;  // 19.95원
		long taxInCents = (priceInCents * 10 + 50) / 100;  // 반올림 포함

		// then
		assertEquals(200, taxInCents);  // 2.00원 (200센트)
		assertEquals(2.0, taxInCents / 100.0);
	}

	@Test
	@Order(3)
	@DisplayName("반올림 공식: (값 × 10^n + 5) / 10^n")
	void roundingFormula() {
		// given
		long priceInCents = 1995;  // 19.95원
		long withTax = (priceInCents * 110 + 50) / 100;  // 10% 추가 + 반올림

		// then
		assertEquals(2195, withTax);  // 21.95원 (2195센트)
		assertEquals(21.95, withTax / 100.0);
	}

	@Test
	@Order(4)
	@DisplayName("반올림 공식 검증: 1995 × 1.1")
	void roundingFormula_calculation() {
		// given
		long priceInCents = 1995;

		// when
		long step1 = priceInCents * 110;  // 219450
		long step2 = step1 + 50;          // 219500
		long result = step2 / 100;        // 2195

		// then
		assertEquals(219450, step1);
		assertEquals(219500, step2);
		assertEquals(2195, result);
	}

	@Test
	@Order(5)
	@DisplayName("평균 계산 - 정수 연산")
	void averageCalculation_integer() {
		// given
		int sum = 85 + 90 + 78;  // 253

		// when
		long avgTimes10 = (sum * 10 + 1) / 3;  // (2530 + 1) / 3 = 843 (소수점 버림)
		long rounded = (avgTimes10 + 5) / 10;   // (843 + 5) / 10 = 84

		// then
		assertEquals(253, sum);
		assertEquals(843, avgTimes10);  // 844가 아니라 843
		assertEquals(84, rounded);
	}

	@Test
	@Order(6)
	@DisplayName("평균 계산 과정 검증")
	void averageCalculation_steps() {
		// given
		int sum = 253;

		// when
		long step1 = sum * 10 + 1;      // 2531
		long step2 = step1 / 3;          // 843 (소수점 버림)
		long step3 = step2 + 5;          // 848
		long result = step3 / 10;        // 84

		// then
		assertEquals(2531, step1);
		assertEquals(843, step2);
		assertEquals(848, step3);
		assertEquals(84, result);
	}

	@Test
	@Order(7)
	@DisplayName("센트 단위 계산은 반올림 오차가 없다")
	void centCalculation_noRoundingError() {
		// given
		long price1 = 1995;  // 19.95
		long price2 = 2495;  // 24.95

		// when
		long tax1 = (price1 * 10 + 50) / 100;
		long tax2 = (price2 * 10 + 50) / 100;

		// then
		assertEquals(200, tax1);  // 정확히 2.00
		assertEquals(250, tax2);  // 정확히 2.50
	}
}
