/*
* класс управления балансом
* */
public class Account {
    public static int balance = 0;
    private static final int waitBalance = 100;

    /*синхронизированный метод увеличивает баланс
     * @param money - количество денег на которое увеличивается баланс
     * */
    public synchronized void topUpMoney(int money){
        balance += money;
        System.out.println("На счете " + Account.balance + " денег");
        notify();
    }

    /*синхронизированный метод уменьшает баланс
    * @param money - количество денег на которое уменьшается баланс
    * */
    public synchronized void withdrawMoney(int money){
        if (money > 0 && balance >= money) {
            balance -= money;
            System.out.printf("Снятие: -%d\n", money);
        }
    }

    /*синхронизиованный метод
    * @param return true если balance меньше порогового значения для снятия, иначе false
    * */
    public synchronized boolean boolWaiting(){
        return balance < waitBalance;
    }

    /*синхронизированный метод выполняется пока баланс меньше порогового значения для снятия
    * проверяет условие и ожидает увеличения баланса
    * */
    public synchronized void waiting() throws InterruptedException {
        while (boolWaiting()) {
            wait();
        }
        Thread.sleep(500);
    }
}
