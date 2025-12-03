package io.github.zeromok.blog.floatingpoint;

import java.math.BigDecimal;

public class FloatingPointError {
	public static void main(String[] args) {
		System.out.println("=== 부동소수점 오차 사례 ===\n");

		demonstrateStorageError();
		demonstrateRoundingError();
		demonstrateSpecificNumbers();
	}

	private static void demonstrateStorageError() {
		System.out.println("1. 표현 오차");
		System.out.println("─────────────────────────────");

		System.out.println("0.1의 실제 저장값:");
		System.out.println(new BigDecimal(0.1));

		System.out.println("\n0.1 + 0.2의 결과:");
		System.out.println(new BigDecimal(0.1 + 0.2));

		System.out.println();
	}

	private static void demonstrateRoundingError() {
		System.out.println("2. 반올림 오차");
		System.out.println("─────────────────────────────");

		double sum = 85 + 90 + 78;
		double average = sum / 3;
		System.out.println("평균 계산: " + (Math.round(average * 10) / 10.0));
		System.out.println("예상: 84.3");

		double price = 24.95;
		double vat = price * 0.1;
		System.out.println("\n24.95의 10%: " + (Math.round(vat * 100) / 100.0));
		System.out.println("예상: 2.49, 실제: 2.5");
		System.out.println();
	}

	private static void demonstrateSpecificNumbers() {
		System.out.println("3. 정확한 숫자 vs 부정확한 숫자");
		System.out.println("─────────────────────────────");

		double accurate = 0.5 + 0.25;
		System.out.println("0.5 + 0.25 == 0.75: " + (accurate == 0.75));

		double inaccurate = 0.1 + 0.2;
		System.out.println("\n0.1 + 0.2 == 0.3: " + (inaccurate == 0.3));
	}
}
