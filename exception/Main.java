import java.util.Scanner;
/** Лаунер */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите число: ");
        String input = in.nextLine();
        try{
            // вызывается метод, проверяющий корректность введенных данных
            ConverterException.ValidCheck(input);
        } catch(ConverterException ex){
            System.out.println(ex.getMessage());
        }
    }
}
