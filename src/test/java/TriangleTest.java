import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



//public class TriangleTest {
//
//    @Test
//    public void testEquilateral() {
//        // Input: 5, 5, 5 | Expected Output: "equilateral"
//        Triangle t = new Triangle(5, 5, 5);
//        assertEquals("equilateral", t.classify(), "All three sides equal should be equilateral");
//    }
//
//    @Test
//    public void testIsosceles() {
//        // Inputs: Permutations of two equal sides | Expected Output: "isosceles"
//        assertEquals("isosceles", new Triangle(5, 5, 3).classify());
//        assertEquals("isosceles", new Triangle(5, 3, 5).classify());
//        assertEquals("isosceles", new Triangle(3, 5, 5).classify());
//    }
//
//    @Test
//    public void testRightAngled() {
//        // Inputs: Permutations of Pythagorean triples | Expected Output: "right-angled"
//        assertEquals("right-angled", new Triangle(3, 4, 5).classify(), "Sorted inputs");
//        assertEquals("right-angled", new Triangle(5, 4, 3).classify(), "Unsorted inputs should still work");
//    }
//
//    @Test
//    public void testScalene() {
//        // Input: 4, 5, 6 | Expected Output: "scalene"
//        Triangle t = new Triangle(4, 5, 6);
//        assertEquals("scalene", t.classify(), "All sides different, no right angle should be scalene");
//    }
//
//    @Test
//    public void testImpossible() {
//        // Inputs: Invalid side lengths | Expected Output: "impossible"
//        assertEquals("impossible", new Triangle(0, 5, 5).classify(), "Zero length is impossible");
//        assertEquals("impossible", new Triangle(-1, 4, 5).classify(), "Negative length is impossible");
//
//        // Inputs: Triangle inequality violations | Expected Output: "impossible"
//        assertEquals("impossible", new Triangle(1, 2, 3).classify(), "1+2=3 violates triangle inequality");
//        assertEquals("impossible", new Triangle(1, 10, 2).classify(), "1+2<10 violates triangle inequality");
//    }
//
//    @Test
//    public void testPerimeter() {
//        // Input: 3, 4, 5 | Expected Output: 12
//        Triangle t = new Triangle(3, 4, 5);
//        assertEquals(12, t.getPerimeter());
//    }
//
//    @Test
//    public void testArea() {
//        // Input: 3, 4, 5 | Expected Output: 6.0
//        Triangle t1 = new Triangle(3, 4, 5);
//        assertEquals(6.0, t1.getArea(), 0.001);
//
//        // Input: Impossible triangle | Expected Output: -1.0
//        Triangle t2 = new Triangle(1, 2, 3);
//        assertEquals(-1.0, t2.getArea(), 0.001);
//    }
//}

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("삼각형 판별 시스템 TDD 명세서")
public class TriangleTest {

    @Nested
    @DisplayName("1. 삼각형 유형 판별 (Classification)")
    class ClassificationTests {

        @ParameterizedTest(name = "입력: {0}, {1}, {2} => 기대 반환값: {3}")
        @CsvSource({
                // 정삼각형
                "5, 5, 5, equilateral",
                // 이등변삼각형 (모든 순열)
                "5, 5, 3, isosceles",
                "5, 3, 5, isosceles",
                "3, 5, 5, isosceles",
                // 직각삼각형 (모든 순열)
                "3, 4, 5, right-angled",
                "5, 4, 3, right-angled",
                "4, 5, 3, right-angled",
                // 부등변삼각형
                "4, 5, 6, scalene"
        })
        @DisplayName("정상적인 삼각형 유형 판별 로직 검증")
        void testValidTriangles(int s1, int s2, int s3, String expected) {
            Triangle t = new Triangle(s1, s2, s3);
            assertEquals(expected, t.classify());
        }

        @ParameterizedTest(name = "입력: {0}, {1}, {2} => 성립 불가(impossible)")
        @CsvSource({
                "0, 5, 5",   // 0 포함
                "-1, 4, 5",  // 음수 포함
                "0, 0, 0",   // 전체 0
                "1, 2, 3",   // 삼각형 부등식 위반 (합이 같음)
                "1, 10, 2"   // 삼각형 부등식 위반 (합이 작음)
        })
        @DisplayName("성립 불가능한 예외 케이스 검증")
        void testImpossibleTriangles(int s1, int s2, int s3) {
            Triangle t = new Triangle(s1, s2, s3);
            assertEquals("impossible", t.classify());
            assertTrue(t.isImpossible());
        }
    }

    @Nested
    @DisplayName("2. 도형 계산 (Calculation)")
    class CalculationTests {

        @Test
        @DisplayName("둘레(Perimeter) 정상 계산 검증")
        void testPerimeter() {
            Triangle t = new Triangle(3, 4, 5);
            assertEquals(12, t.getPerimeter());
        }

        @Test
        @DisplayName("넓이(Area) 정상 및 예외 계산 검증")
        void testArea() {
            Triangle validTriangle = new Triangle(3, 4, 5);
            assertEquals(6.0, validTriangle.getArea(), 0.001, "3,4,5 직각삼각형 넓이는 6.0");

            Triangle impossibleTriangle = new Triangle(1, 2, 3);
            assertEquals(-1.0, impossibleTriangle.getArea(), 0.001, "불가능한 삼각형 넓이는 -1.0");
        }
    }

    @Nested
    @DisplayName("3. 아키텍처 및 강건성 검증 (Robustness)")
    class ArchitectureTests {

        @Test
        @DisplayName("객체의 불변성(Immutability) 유지 검증")
        void testImmutability() {
            // Given: 초기 상태의 삼각형 객체
            Triangle original = new Triangle(3, 4, 5);
            int originalPerimeter = original.getPerimeter();

            // When: 길이를 변경하려고 시도 (새 객체가 반환되어야 함)
            Triangle modified = original.setSideLengths(5, 5, 5);

            // Then: 원본 객체의 상태는 변하지 않아야 함
            assertEquals(originalPerimeter, original.getPerimeter(), "원본 객체의 상태(둘레)가 변하면 안 됩니다.");
            assertEquals(12, original.getPerimeter());

            // 변경된 새 객체는 새로운 상태를 가져야 함
            assertEquals(15, modified.getPerimeter(), "새로 반환된 객체는 변경된 상태를 가져야 합니다.");
            assertNotSame(original, modified, "두 객체는 서로 다른 메모리 주소를 가져야 합니다.");
        }
    }
}