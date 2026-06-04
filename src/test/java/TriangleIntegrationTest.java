import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TriangleIntegrationTest {

    @ParameterizedTest(name = "입력: ({0}, {1}, {2}) => 다중 플래그 통합 검증")
    @CsvSource({
            "3, 3, 3, equilateral",
            "3, 3, 4, isosceles",
            "4, 5, 6, scalene",
            "1, 2, 3, impossible",
            "5, 12, 13, right-angled"
    })
    @DisplayName("실제 상태값과 로직이 결합된 통합 테스트 검증")
    void testGetTypeFlags_Integration(int s1, int s2, int s3, String expectedPrimaryFlag) {
        Triangle t = new Triangle(s1, s2, s3);
        List<String> flags = t.getTypeFlags();

        assertTrue(flags.contains(expectedPrimaryFlag));
    }

    @Test
    @DisplayName("복합 플래그(Right-angled + Scalene) 실제 연산 통합 정밀 검증")
    void testGetTypeFlags_ComplexIntegration() {
        Triangle rightAngledScalene = new Triangle(3, 4, 5);
        List<String> flags = rightAngledScalene.getTypeFlags();

        assertEquals(2, flags.size());
        assertEquals("right-angled", flags.get(0));
        assertEquals("scalene", flags.get(1));
    }
}