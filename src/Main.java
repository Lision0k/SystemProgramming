import java.util.Scanner;
class MultyMethods{
    static int mult1(int a, int b){
        int result = 0;
        for (int i = 0; i < Math.abs(b); i++) {
            result += Math.abs(a);
        }
        return (a < 0 && b > 0) || (a > 0 && b < 0) ? -result : result;
    }

    static int mult2(int a, int b){
        if(a == 0 | b == 0)
            return 0;
        else if(a > 0)
            return b + mult1(a - 1, b);
        else
            return -b + mult1(a + 1, b);
    }

    static int mult3(int a, int b){
        if(a == 0 | b == 0)
            return 0;
        boolean negative = (a < 0) ^ (b < 0);
        a = Math.abs(a);
        b = Math.abs(b);
        int result = 0;
        while (b > 0) {
            if ((b & 1) == 1) {
                result += a;
            }
            a <<= 1;
            b >>= 1;
        }
        return negative ? -result : result;
    }

    public static int mult4(int a, int b) {
        // Использование формулы (a+b)² - a² - b²
        long sumSq = (long) (a + b) * (a + b);
        long aSq = (long) a * a;
        long bSq = (long) b * b;
        return (int) ((sumSq - aSq - bSq) / 2);
    }

    public static int mult5(int a, int b) {
        if (a == 0 || b == 0) return 0;
        if (a == 1) return b;
        if (b == 1) return a;
        if (a == -1) return -b;
        if (b == -1) return -a;
        boolean negative = (a < 0) ^ (b < 0);
        int absA = Math.abs(a);
        int absB = Math.abs(b);
        int half = mult5(absA >> 1, absB);
        int remainder = (absA & 1) == 1 ? absB : 0;
        int result = half + half + remainder;
        return negative ? -result : result;
    }

    public static int mult6(int a, int b) {
        if (a == 0 || b == 0) return 0;
        boolean negative = (a < 0) ^ (b < 0);
        double logResult = Math.log(Math.abs(a)) + Math.log(Math.abs(b));
        int result = (int) Math.round(Math.exp(logResult));
        // Корректировка для точности
        if (Math.abs(a) * Math.abs(b) - result == 1) {
            result++;
        }
        return negative ? -result : result;
    }

    public static int mult7(int a, int b) {
        int result = 0;
        int count = Math.abs(b);
        while (count-- > 0) {
            result += a;
        }
        return b < 0 ? -result : result;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите первый множитель: \na = ");
        int a = in.nextInt();
        System.out.print("Введите второй множитель: \nb = ");
        int b = in.nextInt();

        System.out.println("a * b = " + MultyMethods.mult1(a, b));
        System.out.println("a * b = " + MultyMethods.mult2(a, b));
        System.out.println("a * b = " + MultyMethods.mult3(a, b));
        System.out.println("a * b = " + MultyMethods.mult4(a, b));
        System.out.println("a * b = " + MultyMethods.mult5(a, b));
        System.out.println("a * b = " + MultyMethods.mult6(a, b));
        System.out.println("a * b = " + MultyMethods.mult7(a, b));
    }
}