import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Triangle 클래스
 * 과제3 CI 업데이트: 다중 속성을 반환하는 getTypeFlags() 아키텍처 적용
 */
public class Triangle
{
    private final int side1;
    private final int side2;
    private final int side3;
    private final long[] sortedSides;

    public enum Type {
        EQUILATERAL("equilateral"),
        ISOSCELES("isosceles"),
        RIGHT_ANGLED("right-angled"),
        SCALENE("scalene"),
        IMPOSSIBLE("impossible");

        private final String label;
        Type(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public Triangle(int s1, int s2, int s3)
    {
        this.side1 = s1;
        this.side2 = s2;
        this.side3 = s3;

        this.sortedSides = new long[] { s1, s2, s3 };
        Arrays.sort(this.sortedSides);
    }

    public Triangle setSideLengths(int s1, int s2, int s3)
    {
        return new Triangle(s1, s2, s3);
    }

    public String getSideLengths()
    {
        return side1 + "," + side2 + "," + side3;
    }

    public long getPerimeter()
    {
        return (long) side1 + side2 + side3;
    }

    public double getArea()
    {
        if (isImpossible())
        {
            return -1.0;
        }
        double s = getPerimeter() / 2.0;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    public String classify()
    {
        if (isImpossible()) return Type.IMPOSSIBLE.getLabel();
        if (side1 == side2 && side2 == side3) return Type.EQUILATERAL.getLabel();
        if (side1 == side2 || side1 == side3 || side2 == side3) return Type.ISOSCELES.getLabel();
        if (isRightAngled()) return Type.RIGHT_ANGLED.getLabel();

        return Type.SCALENE.getLabel();
    }

    /**
     * [과제3 신규 추가]: 삼각형의 복수 속성을 조합하여 반환 (확장성 고려)
     * 예: (3,4,5) -> ["right-angled", "scalene"]
     */
    public List<String> getTypeFlags()
    {
        if (isImpossible()) {
            return Collections.singletonList(Type.IMPOSSIBLE.getLabel());
        }

        List<String> flags = new ArrayList<>();

        // 직각삼각형 여부를 우선 판별하여 리스트 최상단에 배치
        if (isRightAngled()) {
            flags.add(Type.RIGHT_ANGLED.getLabel());
        }

        // 변의 길이에 따른 배타적 속성 판별 로직
        if (side1 == side2 && side2 == side3) {
            flags.add(Type.EQUILATERAL.getLabel());
        } else if (side1 == side2 || side1 == side3 || side2 == side3) {
            flags.add(Type.ISOSCELES.getLabel());
        } else {
            flags.add(Type.SCALENE.getLabel());
        }

        return flags; // 불변을 원한다면 Collections.unmodifiableList(flags) 반환 권장
    }

    public boolean isRightAngled()
    {
        return sortedSides[2] * sortedSides[2] == sortedSides[0] * sortedSides[0] + sortedSides[1] * sortedSides[1];
    }

    public boolean isImpossible()
    {
        if (side1 <= 0 || side2 <= 0 || side3 <= 0) return true;
        return sortedSides[0] + sortedSides[1] <= sortedSides[2];
    }
}