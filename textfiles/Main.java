import java.io.File;

/** 
 * Лаунчер 
 * */
public class Main {
    public static void main(String[] args) {
        // Задача 2
        XYZFile first = new XYZFile(new File("XYZ"));
        System.out.println("Длина самой длинной последовательности, состоящей из символов X: " + first.XSeria());

        // Задача 12
        ABCZFile second = new ABCZFile(new File("ABCZ"));
        System.out.println("Символ, который чаще всего встречается между двумя одинаковыми символами: " + second.InCombinations());

        // Задача 32
        NumsFile third = new NumsFile(new File("Nums"));
        System.out.println("Максимальное количество идущих подряд цифр, удовлетворяющих условию: " + third.CountNums());

    }
}
