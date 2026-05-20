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
                "5, 5, 5, equilateral",
                "5, 5, 3, isosceles",
                "5, 3, 5, isosceles",
                "3, 5, 5, isosceles",
                "3, 4, 5, right-angled",
                "5, 4, 3, right-angled",
                "4, 5, 6, scalene"
        })
        void testValidTriangles(int s1, int s2, int s3, String expected) {
            Triangle t = new Triangle(s1, s2, s3);
            assertEquals(expected, t.classify());
        }

        @ParameterizedTest(name = "입력: {0}, {1}, {2} => 성립 불가(impossible)")
        @CsvSource({
                "0, 5, 5",
                "-1, 4, 5",
                "1, 2, 3",
                "1, 10, 2"
        })
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
        void testPerimeter() {
            Triangle t = new Triangle(3, 4, 5);
            // [정적 분석 조치 반영]: getPerimeter()의 반환형이 long으로 변경됨에 따라 단정문 타입 엄격성 강화
            assertEquals(12L, t.getPerimeter());
        }

        @Test
        void testArea() {
            Triangle validTriangle = new Triangle(3, 4, 5);
            assertEquals(6.0, validTriangle.getArea(), 0.001);

            Triangle impossibleTriangle = new Triangle(1, 2, 3);
            assertEquals(-1.0, impossibleTriangle.getArea(), 0.001);
        }
    }

    @Nested
    @DisplayName("3. 아키텍처 및 강건성 검증 (Robustness)")
    class ArchitectureTests {

        // [아키텍처 개선 반영]: 상태 변경 시 객체가 변이되지 않고(Not Same), 값만 보장되는지 확인하는 불변성 테스트 신규 추가
        @Test
        void testImmutability() {
            Triangle original = new Triangle(3, 4, 5);
            long originalPerimeter = original.getPerimeter();

            Triangle modified = original.setSideLengths(5, 5, 5);

            assertEquals(originalPerimeter, original.getPerimeter());
            assertEquals(15L, modified.getPerimeter());
            assertNotSame(original, modified);
        }
    }
}