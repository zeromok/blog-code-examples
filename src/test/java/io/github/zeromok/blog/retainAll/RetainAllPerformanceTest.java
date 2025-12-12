package io.github.zeromok.blog.retainAll;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RetainAllPerformanceTest {
	@Test
	@Order(1)
	@DisplayName("ArrayList.retainAll() - O(n×m) 동작 확인")
	void arrayListRetainAll() {
		// given
		List<Integer> list1 = createSequentialList(10_000);
		List<Integer> list2 = createSequentialList(5_000, 10_000);  // 5000~15000

		// when
		long startTime = System.nanoTime();
		list1.retainAll(list2);
		long endTime = System.nanoTime();

		// then
		assertThat(list1).hasSize(5_000);  // 교집합: 5000~10000
		System.out.printf("ArrayList.retainAll(): %d ms%n", (endTime - startTime) / 1_000_000);
	}

	@Test
	@Order(2)
	@DisplayName("HashSet.retainAll() - O(n) 동작 확인")
	void hashSetRetainAll() {
		// given
		List<Integer> list1 = createSequentialList(10_000);
		List<Integer> list2 = createSequentialList(5_000, 10_000);

		Set<Integer> set1 = new HashSet<>(list1);
		Set<Integer> set2 = new HashSet<>(list2);

		// when
		long startTime = System.nanoTime();
		set1.retainAll(set2);
		long endTime = System.nanoTime();

		// then
		assertThat(set1).hasSize(5_000);
		System.out.printf("HashSet.retainAll(): %d ms%n", (endTime - startTime) / 1_000_000);
	}

	@Test
	@Order(3)
	@DisplayName("Stream + HashSet contains - O(n) 동작 + 순서 보장")
	void streamWithHashSetContains() {
		// given
		List<Integer> list1 = createSequentialList(10_000);
		List<Integer> list2 = createSequentialList(5_000, 10_000);

		Set<Integer> set2 = new HashSet<>(list2);

		// when
		long startTime = System.nanoTime();
		List<Integer> result = list1.stream()
			.filter(set2::contains)
			.collect(Collectors.toList());
		long endTime = System.nanoTime();

		// then
		assertThat(result).hasSize(5_000);
		assertThat(result).isSorted();  // 순서 보장 확인
		System.out.printf("Stream + HashSet: %d ms%n", (endTime - startTime) / 1_000_000);
	}

	@Test
	@Order(4)
	@DisplayName("성능 비교 벤치마크 - 데이터 크기별")
	void performanceBenchmark() {
		int[] dataSizes = {100, 1_000, 10_000, 100_000};

		System.out.println("=== 성능 비교 (데이터 크기별) ===");
		System.out.printf("%-10s | %-15s | %-15s | %-15s%n",
			"Size", "ArrayList(ms)", "HashSet(ms)", "Stream+Set(ms)");
		System.out.println("-".repeat(70));

		for (int size : dataSizes) {
			List<Integer> list1 = createSequentialList(size);
			List<Integer> list2 = createSequentialList(size / 2, size / 2);

			// ArrayList
			List<Integer> testList1 = new ArrayList<>(list1);
			long start1 = System.nanoTime();
			testList1.retainAll(list2);
			long time1 = (System.nanoTime() - start1) / 1_000_000;

			// HashSet
			Set<Integer> set1 = new HashSet<>(list1);
			Set<Integer> set2 = new HashSet<>(list2);
			long start2 = System.nanoTime();
			set1.retainAll(set2);
			long time2 = (System.nanoTime() - start2) / 1_000_000;

			// Stream
			Set<Integer> filterSet = new HashSet<>(list2);
			long start3 = System.nanoTime();
			List<Integer> result = list1.stream()
				.filter(filterSet::contains)
				.collect(Collectors.toList());
			long time3 = (System.nanoTime() - start3) / 1_000_000;

			System.out.printf("%-10s | %-15d | %-15d | %-15d%n",
				formatSize(size), time1, time2, time3);
		}
	}

	@Test
	@Order(5)
	@DisplayName("동작 정확성 검증 - 중복 요소 포함")
	void verifyBehaviorWithDuplicates() {
		// given
		List<Integer> list1 = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5);
		List<Integer> list2 = Arrays.asList(2, 3, 6, 7);

		// ArrayList - 중복 유지
		List<Integer> arrayListResult = new ArrayList<>(list1);
		arrayListResult.retainAll(list2);

		// HashSet - 중복 제거
		Set<Integer> hashSetResult = new HashSet<>(list1);
		hashSetResult.retainAll(new HashSet<>(list2));

		// Stream - 중복 유지 + 순서 보장
		Set<Integer> set2 = new HashSet<>(list2);
		List<Integer> streamResult = list1.stream()
			.filter(set2::contains)
			.collect(Collectors.toList());

		// then
		assertThat(arrayListResult).containsExactly(2, 2, 3, 3, 3);  // 중복 유지
		assertThat(hashSetResult).containsExactlyInAnyOrder(2, 3);   // 중복 제거
		assertThat(streamResult).containsExactly(2, 2, 3, 3, 3);     // 중복 유지 + 순서
	}

	@Test
	@Order(6)
	@DisplayName("최악의 경우 - 교집합이 없는 경우")
	void worstCase_NoIntersection() {
		// given
		List<Integer> list1 = createSequentialList(10_000);          // 0~9999
		List<Integer> list2 = createSequentialList(10_000, 10_000);  // 10000~19999

		// ArrayList
		List<Integer> testList = new ArrayList<>(list1);
		long start1 = System.nanoTime();
		testList.retainAll(list2);
		long time1 = (System.nanoTime() - start1) / 1_000_000;

		// HashSet
		Set<Integer> set1 = new HashSet<>(list1);
		Set<Integer> set2 = new HashSet<>(list2);
		long start2 = System.nanoTime();
		set1.retainAll(set2);
		long time2 = (System.nanoTime() - start2) / 1_000_000;

		// then
		assertThat(testList).isEmpty();
		assertThat(set1).isEmpty();

		System.out.printf("최악의 경우 (교집합 없음):%n");
		System.out.printf("ArrayList: %d ms%n", time1);
		System.out.printf("HashSet: %d ms%n", time2);
		System.out.printf("성능 차이: %.1f배%n", (double) time1 / time2);
	}


	// === Helper Methods ===
	/// # 0 부터 시작하는 순차 리스트 생성
	private List<Integer> createSequentialList(int size) {
		return createSequentialList(0, size);
	}

	/// # start 부터 size 개의 순차 리스트 생성
	private List<Integer> createSequentialList(int start, int size) {
		List<Integer> list = new ArrayList<>(size);
		for (int i = start; i < start + size; i++) {
			list.add(i);
		}
		return list;
	}

	/// # 숫자 포맷 메서드
	private String formatSize(int size) {
		if (size >= 1_000_000) {
			return (size / 1_000_000) + "M";
		} else if (size >= 1_000) {
			return (size / 1_000) + "K";
		}
		return String.valueOf(size);
	}
}
