import java.util.Arrays;

public class Triangle {
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

    public Triangle(int s1, int s2, int s3) {
        this.side1 = s1;
        this.side2 = s2;
        this.side3 = s3;

        this.sortedSides = new long[] { s1, s2, s3 };
        Arrays.sort(this.sortedSides);
    }

    public Triangle setSideLengths(int s1, int s2, int s3) {
        return new Triangle(s1, s2, s3);
    }

    public String getSideLengths() {
        return side1 + "," + side2 + "," + side3;
    }

    // [정적 분석 조치]: 잠재적 정수 오버플로우 방지를 위해 반환 타입을 int에서 long으로 변경 및 형변환
    public long getPerimeter() {
        return (long) side1 + side2 + side3;
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