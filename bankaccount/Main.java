public class Main {
    public static void main(String[] args) throws InterruptedException {
        AccountThread thread = new AccountThread();
        //запуск потока увеличивающего баланс
        thread.start();
        //ожидаем увеличения до порогового значения и выше
        AccountThread.account.waiting();
        int a = (int) (Math.random() * 99) + 1;
        //вызываем метод снятия денег со счета
        AccountThread.account.withdrawMoney(a);
        System.out.println("На счете осталось " + Account.balance + " денег");
    }
}
