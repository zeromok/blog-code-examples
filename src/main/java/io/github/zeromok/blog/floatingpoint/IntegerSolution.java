package io.github.zeromok.blog.floatingpoint;

public class IntegerSolution {
	public static void main(String[] args) {
		System.out.println("=== 해결책 1: 정수로 변환 ===\n");

		compareDoubleVsInteger();
		demonstrateRoundingFormula();
		demonstrateAverageCalculation();
	}

	/// double vs 정수 비교
	private static void compareDoubleVsInteger() {
		System.out.println("1. double vs 정수 비교");
		System.out.println("─────────────────────────────");

		// double 계산
		double price = 19.95;
		double tax = price * 0.1;
		System.out.println("double 계산");
		System.out.println("   실제값: " + new java.math.BigDecimal(tax));
		System.out.println("   반올림: " + (Math.round(tax * 100) / 100.0));

		// 정수 계산
		long priceInCents = 1995;
		long taxInCents = (priceInCents * 10 + 50) / 100;
		System.out.println("\n정수 계산");
		System.out.println("   실제값: " + taxInCents + "센트");
		System.out.println("   반올림: " + (taxInCents / 100.0));

		System.out.println();
	}

	/// 반올림 공식: (값 × 10^n + 5) / 10^n
	private static void demonstrateRoundingFormula() {
		System.out.println("2. 반올림 공식");
		System.out.println("─────────────────────────────");
		System.out.println("공식: (값 × 10^n + 5) / 10^n");

		long priceInCents = 1995;  // 19.95원
		long withTax = (priceInCents * 110 + 50) / 100;  // 10% 추가
		System.out.println("\n1995센트 × 1.1 = " + (withTax / 100.0) + "원");
		System.out.println("계산: (1995 × 110 + 50) / 100 = " + withTax + "센트");

		System.out.println();
	}

	/// 실전: 평균 계산
	private static void demonstrateAverageCalculation() {
		System.out.println("3. 평균 계산 예제");
		System.out.println("─────────────────────────────");

		int sum = 85 + 90 + 78;  // 253
		System.out.println("합계: " + sum);

		long avgTimes10 = (sum * 10 + 1) / 3;
		long rounded = (avgTimes10 + 5) / 10;
		System.out.println("평균 (정수 연산): " + rounded);

		System.out.println("\n계산 과정:");
		System.out.println("1. (253 × 10 + 1) / 3 = " + avgTimes10);
		System.out.println("2. (" + avgTimes10 + " + 5) / 10 = " + rounded);

		System.out.println();
	}
}
