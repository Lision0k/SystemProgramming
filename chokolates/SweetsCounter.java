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
    public void profitCount(){
        int counter, price;
        counter = this.money/this.price;
        if(counter % this.wrap == 0)
            System.out.println("Покупка выгодна");
        else{
            int remainder = counter%this.wrap;
            if(this.wrap-remainder < remainder){
                //округляем в большую сторону
                counter += this.wrap-remainder;
                price = counter*this.price;
                counter += counter/this.wrap;
                System.out.printf("Выгоднее купить %d конфет, потратив %d$", counter, price);
            }
            else{
                //округляем в меньшую сторону
                counter -= remainder;
                price = counter*this.price;
                counter += counter/this.wrap;
                System.out.printf("Выгоднее купить %d конфет, потратив %d$", counter, price);
            }
        }
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
        System.out.printf("В сумме получилось %d шоколадных конфет\n", shop.count());
        shop.profitCount();
    }
}
