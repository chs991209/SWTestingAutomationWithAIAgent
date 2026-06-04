import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Triangle {
    // 1. 객체의 불변성(Immutability) 보장: final 키워드 사용
    private final int side1;
    private final int side2;
    private final int side3;
    private final long[] sortedSides; // 성능 최적화를 위한 배열 캐싱

    // 2. 타입 안정성을 위한 Enum
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

    public Triangle(int s1, int s2, int s3) {
        this.side1 = s1;
        this.side2 = s2;
        this.side3 = s3;

        this.sortedSides = new long[] { s1, s2, s3 };
        Arrays.sort(this.sortedSides);
    }

    // [아키텍처 개선]: 상태 변경(Mutation) 방지 - 불변 객체 패턴 적용
    public Triangle setSideLengths(int s1, int s2, int s3) {
        return new Triangle(s1, s2, s3);
    }

    public String getSideLengths() {
        return side1 + "," + side2 + "," + side3;
    }

    // [정적 분석 조치]: 정수 오버플로우 방지를 위해 long 반환
    public long getPerimeter() {
        return (long) side1 + side2 + side3;
    }

    // 넓이 계산 (Heron의 공식)
    public double getArea() {
        if (isImpossible()) {
            return -1.0; // 요구사항 컨트랙트에 따른 에러 코드 반환
        }
        double s = getPerimeter() / 2.0;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    // [기존 요구사항]: 단일 속성 반환 메서드
    public String classify() {
        if (isImpossible()) return Type.IMPOSSIBLE.getLabel();
        if (side1 == side2 && side2 == side3) return Type.EQUILATERAL.getLabel();
        if (side1 == side2 || side1 == side3 || side2 == side3) return Type.ISOSCELES.getLabel();
        if (isRightAngled()) return Type.RIGHT_ANGLED.getLabel();

        return Type.SCALENE.getLabel();
    }

    // [과제 3 신규 통합]: 다중 속성 반환 메서드
    public List<String> getTypeFlags() {
        if (isImpossible()) {
            return Collections.singletonList(Type.IMPOSSIBLE.getLabel());
        }

        List<String> flags = new ArrayList<>();

        // 직각 속성 우선 판별
        if (isRightAngled()) {
            flags.add(Type.RIGHT_ANGLED.getLabel());
        }

        // 변의 길이에 따른 배타적 속성 판별
        if (side1 == side2 && side2 == side3) {
            flags.add(Type.EQUILATERAL.getLabel());
        } else if (side1 == side2 || side1 == side3 || side2 == side3) {
            flags.add(Type.ISOSCELES.getLabel());
        } else {
            flags.add(Type.SCALENE.getLabel());
        }

        return flags;
    }

    // 삼각형 부등식 및 수학 검증 모듈
    public boolean isRightAngled() {
        return sortedSides[2] * sortedSides[2] == sortedSides[0] * sortedSides[0] + sortedSides[1] * sortedSides[1];
    }

    public boolean isImpossible() {
        if (side1 <= 0 || side2 <= 0 || side3 <= 0) return true;
        // 미리 정렬된 배열을 활용한 안전한 부등식 검증
        return sortedSides[0] + sortedSides[1] <= sortedSides[2];
    }
}