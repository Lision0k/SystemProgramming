/*
* класс потока, который увеличивает баланс
* */
public class AccountThread extends Thread{
    public static Account account = new Account();
    @Override
    public  void run(){
        while (account.boolWaiting()) {
            int a = (int) (Math.random() * 99) + 1;
                account.topUpMoney(a);
        }
    }
}
