// src/main/java/Triangle.java
import java.util.Arrays;

public class Triangle {
    private int side1, side2, side3;

    // Constants matching the expected outputs in the tests
    private static final String P_EQUILATERAL = "equilateral";
    private static final String P_ISOSCELES   = "isosceles";
    private static final String P_RIGHTANGLED = "right-angled";
    private static final String P_SCALENE     = "scalene";
    private static final String P_IMPOSSIBLE  = "impossible";

    public Triangle(int s1, int s2, int s3) {
        this.side1 = s1;
        this.side2 = s2;
        this.side3 = s3;
    }

//    public Triangle setSideLengths(int s1, int s2, int s3) {
//        this.side1 = s1;
//        this.side2 = s2;
//        this.side3 = s3;
//        return this;
//    }
    // 수정 부위
    public Triangle setSideLengths(int s1, int s2, int s3) {
        // 기존 상태를 덮어쓰지 않고, 새로운 Triangle 객체를 생성하여 반환합니다.
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
        // Using 2.0 to avoid integer division truncation
        double s = getPerimeter() / 2.0;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    public String classify() {
        // Must check impossible first, as defined by tests
        if (isImpossible()) {
            return P_IMPOSSIBLE;
        }

        if (side1 == side2 && side2 == side3) {
            return P_EQUILATERAL;
        }

        if (side1 == side2 || side1 == side3 || side2 == side3) {
            return P_ISOSCELES;
        }

        if (isRightAngled()) {
            return P_RIGHTANGLED;
        }

        return P_SCALENE;
    }

    public boolean isRightAngled() {
        // Sorting ensures we can find the hypotenuse regardless of input order
        long[] sides = new long[] { side1, side2, side3 };
        Arrays.sort(sides);

        // a^2 + b^2 = c^2 (using long to prevent overflow)
        return sides[2] * sides[2] == sides[0] * sides[0] + sides[1] * sides[1];
    }

    public boolean isImpossible() {
        // Check for 0 or negative
        if (side1 <= 0 || side2 <= 0 || side3 <= 0) {
            return true;
        }
        // Check triangle inequality (sum of two sides must be > third side)
        long s1 = side1, s2 = side2, s3 = side3;
        if (s1 + s2 <= s3 || s1 + s3 <= s2 || s2 + s3 <= s1) {
            return true;
        }
        return false;
    }
}