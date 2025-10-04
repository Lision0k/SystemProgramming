import java.util.Scanner;
class AnimalThread extends Thread{
    public String name;
    public static int distance = 0;
    public static int owndistance = 0;
    private static AnimalThread leader;
    AnimalThread(String name, int priority){
        setPriority(priority);
        this.name = name;
    }
    @Override
    public void run(){
        while(owndistance < distance) {
            owndistance += 2;
            System.out.printf("%S has run %d m; priority = %d\n",name, owndistance, getPriority());
            changePriority();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(this.name + " finished");
    }
    void changePriority(){
        if (leader == this)
            setPriority(Thread.MAX_PRIORITY);
        else
            setPriority(Thread.NORM_PRIORITY);
        if (leader == null || distance > leader.owndistance)
            leader = this;
    }
}
public class Main {
    public static void main(String[] args) {
        AnimalThread rabbit = new AnimalThread("кролик", Thread.NORM_PRIORITY);
        AnimalThread turtle = new AnimalThread("черепаха", Thread.MAX_PRIORITY);
        Scanner in = new Scanner(System.in);
        System.out.print("Введите дистанцию забега: ");
        AnimalThread.distance = in.nextInt();
        rabbit.start();
        turtle.start();
    }
}
