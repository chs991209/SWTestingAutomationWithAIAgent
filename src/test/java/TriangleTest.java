import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("삼각형 판별 시스템 TDD 명세서")
public class TriangleTest {

    @Nested
    @DisplayName("1. 삼각형 유형 판별 (Classification)")
    class ClassificationTests {

        @ParameterizedTest(name = "입력: {0}, {1}, {2} => 기대 반환값: {3}")
        @CsvSource({
                "5, 5, 5, equilateral",
                "5, 5, 3, isosceles",
                "5, 3, 5, isosceles",
                "3, 5, 5, isosceles",
                "3, 4, 5, right-angled",
                "5, 4, 3, right-angled",
                "4, 5, 3, right-angled",
                "4, 5, 6, scalene"
        })
        @DisplayName("정상적인 삼각형 유형 판별 로직 검증")
        void testValidTriangles(int s1, int s2, int s3, String expected) {
            Triangle t = new Triangle(s1, s2, s3);
            assertEquals(expected, t.classify());
        }

        @ParameterizedTest(name = "입력: {0}, {1}, {2} => 성립 불가(impossible)")
        @CsvSource({
                "0, 5, 5",
                "-1, 4, 5",
                "0, 0, 0",
                "1, 2, 3",
                "1, 10, 2"
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
            assertEquals(12, new Triangle(3, 4, 5).getPerimeter());
        }

        @Test
        @DisplayName("넓이(Area) 정상 및 예외 계산 검증")
        void testArea() {
            assertEquals(6.0, new Triangle(3, 4, 5).getArea(), 0.001);
            assertEquals(-1.0, new Triangle(1, 2, 3).getArea(), 0.001);
        }
    }

    @Nested
    @DisplayName("3. 아키텍처 및 강건성 검증 (Robustness)")
    class ArchitectureTests {

        @Test
        @DisplayName("객체의 불변성(Immutability) 유지 검증")
        void testImmutability() {
            Triangle original = new Triangle(3, 4, 5);
            int originalPerimeter = original.getPerimeter();

            Triangle modified = original.setSideLengths(5, 5, 5);

            assertEquals(originalPerimeter, original.getPerimeter(), "원본 객체의 상태는 변하지 않아야 함");
            assertEquals(15, modified.getPerimeter(), "새로 반환된 객체는 변경된 상태를 가져야 함");
            assertNotSame(original, modified, "두 객체는 서로 다른 메모리 주소를 가져야 함");
        }
    }
}