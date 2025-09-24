class NewThread extends Thread{
    Thread thread;
    public NewThread(String name) {
        thread = new Thread(this, name);
        System.out.println(thread);
    }
    @Override
    public void run() {
        System.out.println("поток " + thread.getName() + " запущен");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("поток " + thread.getName() + " завершен");
    }
}
public class Main {
    public static void main(String[] args) {
        String[] names = {"first", "second", "third", "four", "five", "six", "seven", "eight", "nine", "ten"};
        for (String name : names) {
            new NewThread(name).start();
        }
    }
}
