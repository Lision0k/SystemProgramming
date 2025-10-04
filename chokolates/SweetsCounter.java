import java.util.Scanner;
class SweetsCounter{
    int money;
    int price;
    int wrap;
    SweetsCounter(int money, int price, int wrap){
        this.money = money;
        this.price = price;
        this.wrap = wrap;
    }
    public int count(){
        int counter = 0;
        counter += this.money/this.price;
        counter += counter/this.wrap;
        return counter;
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Денег в наличии: ");
        int money = in.nextInt();
        System.out.print("Стоимость одной шоколадки: ");
        int price = in.nextInt();
        System.out.print("Количество оберток за шоколадку: ");
        int wrap = in.nextInt();
        Sweets shop = new Sweets(money, price, wrap);
        System.out.println(shop.count());
    }
}
