import java.util.Arrays;

public class Triangle {
    // 1. 객체의 불변성(Immutability) 보장: final 키워드 사용
    private final int side1;
    private final int side2;
    private final int side3;

    // 미리 정렬된 배열을 캐싱하여 반복 연산 제거
    private final long[] sortedSides;

    // 2. 타입 안정성을 위한 Enum 도입 (오타 원천 차단)
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

    public Triangle setSideLengths(int s1, int s2, int s3) {
        // 기존 상태를 변경하지 않고 새로운 객체 반환 (불변성)
        return new Triangle(s1, s2, s3);
    }

    public String getSideLengths() {
        return side1 + "," + side2 + "," + side3;
    }

    public int getPerimeter() {
        return side1 + side2 + side3;
    }

    public double getArea() {
        if (isImpossible()) {
            return -1.0;
        }
        double s = getPerimeter() / 2.0;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    public String classify() {
        if (isImpossible()) return Type.IMPOSSIBLE.getLabel();

        if (side1 == side2 && side2 == side3) return Type.EQUILATERAL.getLabel();
        if (side1 == side2 || side1 == side3 || side2 == side3) return Type.ISOSCELES.getLabel();
        if (isRightAngled()) return Type.RIGHT_ANGLED.getLabel();

        return Type.SCALENE.getLabel();
    }

    public boolean isRightAngled() {
        return sortedSides[2] * sortedSides[2] == sortedSides[0] * sortedSides[0] + sortedSides[1] * sortedSides[1];
    }

    public boolean isImpossible() {
        if (side1 <= 0 || side2 <= 0 || side3 <= 0) return true;
        return sortedSides[0] + sortedSides[1] <= sortedSides[2];
    }
}