import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * [과제3 - Unit Testing]: Mockito 5.17.0을 활용한 완벽한 모듈 격리
 * * 아키텍처 목적: getTypeFlags() 메서드의 조건 분기 로직만을 '단위 테스트'하기 위해,
 * 수학 연산을 수행하는 isRightAngled()와 isImpossible()을 Mocking(Spy)하여 의존성을 단절시킵니다.
 */
@ExtendWith(MockitoExtension.class)
public class TriangleUnitTest {

    // 실제 객체를 생성하되, 특정 메서드만 행위를 재정의(Stub)하기 위해 @Spy 사용
    @Spy
    private Triangle spyTriangle = new Triangle(4, 5, 6);

    @Test
    @DisplayName("Mocking: 수학 연산과 무관하게 플래그 조합 로직(Right-angled + Scalene)이 정확한지 검증")
    void testGetTypeFlags_RightAngled_Scalene_Mocked() {
        // Given: 실제 (4,5,6)은 직각삼각형이 아니지만, Mocking을 통해 직각이라고 강제 설정
        doReturn(true).when(spyTriangle).isRightAngled();
        doReturn(false).when(spyTriangle).isImpossible();

        // When: 타겟 메서드 실행
        List<String> flags = spyTriangle.getTypeFlags();

        // Then: 상태값(4,5,6 -> scalene)과 Mocking된 값(right-angled -> true)이 올바르게 조합되는지 확인
        assertEquals(2, flags.size());
        assertTrue(flags.contains("right-angled"));
        assertTrue(flags.contains("scalene"));

        // 행위 검증 (Behavior Verification)
        verify(spyTriangle, times(1)).isRightAngled();
    }

    @Test
    @DisplayName("Mocking: Impossible 상태일 경우 다른 플래그 로직을 우회하고 즉시 반환하는지 검증 (경계 확인)")
    void testGetTypeFlags_Impossible_Mocked_Boundary() {
        // Given: 정상적인 (4,5,6) 상태지만 강제로 Impossible 상황 연출 (시스템 장애 시뮬레이션)
        doReturn(true).when(spyTriangle).isImpossible();

        // When
        List<String> flags = spyTriangle.getTypeFlags();

        // Then: 오직 "impossible" 플래그만 존재해야 함
        assertEquals(1, flags.size());
        assertEquals("impossible", flags.get(0));

        // isImpossible에서 true가 반환되었으므로 isRightAngled는 호출조차 되지 않아야 함(단락 평가 검증)
        verify(spyTriangle, never()).isRightAngled();
    }
}