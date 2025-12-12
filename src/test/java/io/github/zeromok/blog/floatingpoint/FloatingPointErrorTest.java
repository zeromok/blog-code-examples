package io.github.zeromok.blog.floatingpoint;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@DisplayName("부동소수점 오차 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FloatingPointErrorTest {

	@Test
	@Order(1)
	@DisplayName("0.1의 실제 저장값은 정확하지 않다")
	void storageError_0_1() {
		// given
		double value = 0.1;
		BigDecimal actual = new BigDecimal(value);

		// then
		assertNotEquals(new BigDecimal("0.1"), actual);
		assertTrue(actual.toString().startsWith("0.1000000000000000055"));
	}

	@Test
	@Order(2)
	@DisplayName("0.1 + 0.2는 0.3이 아니다")
	void storageError_addition() {
		// given
		double result = 0.1 + 0.2;

		// then
		assertNotEquals(0.3, result);
		assertEquals(0.30000000000000004, result);
	}

	@Test
	@Order(3)
	@DisplayName("평균 계산 시 반올림 오차 발생")
	void roundingError_average() {
		// given
		double sum = 85 + 90 + 78;
		double average = sum / 3;
		double rounded = Math.round(average * 10) / 10.0;

		// then
		assertEquals(84.3, rounded);
		// average는 84.33333... 이므로 정확히 84.3이 아님
		assertNotEquals(84.3, average);
	}

	@Test
	@Order(4)
	@DisplayName("24.95의 10% 계산 시 반올림 오차")
	void roundingError_percentage() {
		// given
		double price = 24.95;
		double vat = price * 0.1;
		double rounded = Math.round(vat * 100) / 100.0;

		// then
		assertEquals(2.5, rounded);  // 예상은 2.49지만 실제는 2.5
	}

	@Test
	@Order(5)
	@DisplayName("2의 거듭제곱 분수는 정확하게 표현된다")
	void accurateNumbers() {
		// given
		double result = 0.5 + 0.25;

		// then
		assertEquals(0.75, result);
		assertTrue(result == 0.75);  // 동등 비교 가능
	}

	@Test
	@Order(6)
	@DisplayName("0.1 + 0.2는 0.3과 동등 비교 불가")
	void inaccurateNumbers() {
		// given
		double result = 0.1 + 0.2;

		// then
		assertNotEquals(0.3, result);
		assertFalse(result == 0.3);  // 동등 비교 불가
	}
}
