class MyThread extends Thread{
    String name;
    static String winner;
    MyThread(String name){
        this.name = name;
    }
    @Override
    public void run(){
        for (int i = 0; i < 5; i++) {
            System.out.println(this.name);
            winner = this.name;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
public class Main {
    public static void main(String[] args) {
        System.out.println("Что появилось сначала - яйцо или курица?");
        MyThread chicken = new MyThread("Курица");
        MyThread egg = new MyThread("Яйцо");
        chicken.start();
        egg.start();
        try {
            chicken.join();
            egg.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Спор окончен. Победитель: %s", MyThread.winner);
    }
}
