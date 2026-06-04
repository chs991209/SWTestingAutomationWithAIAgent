import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TriangleUnitTest {

    @Spy
    private Triangle spyTriangle = new Triangle(4, 5, 6);

    @Test
    @DisplayName("Mocking: 플래그 조합 로직(Right-angled + Scalene) 고립 검증")
    void testGetTypeFlags_RightAngled_Scalene_Mocked() {
        doReturn(true).when(spyTriangle).isRightAngled();
        doReturn(false).when(spyTriangle).isImpossible();

        List<String> flags = spyTriangle.getTypeFlags();

        assertEquals(2, flags.size());
        assertTrue(flags.contains("right-angled"));
        assertTrue(flags.contains("scalene"));
        verify(spyTriangle, times(1)).isRightAngled();
    }

    @Test
    @DisplayName("Mocking: Impossible 상태 시 단락 평가(Fail-fast) 검증")
    void testGetTypeFlags_Impossible_Mocked_Boundary() {
        doReturn(true).when(spyTriangle).isImpossible();

        List<String> flags = spyTriangle.getTypeFlags();

        assertEquals(1, flags.size());
        assertEquals("impossible", flags.get(0));
        // isImpossible이 true이므로, 이후 로직은 호출되지 않아야 함
        verify(spyTriangle, never()).isRightAngled();
    }
}