import java.util.Scanner;
public class Main {
    private static final Scanner in = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("""
                Выберете операцию (1 – 3)
                1 – запустить экземпляр процесса
                2 – получить информацию о процессах
                3 – завершить процесс""");
        while(true) {
            String act = in.nextLine();
            switch (act) {
                case "1": createProcess(); break;
                case "2": showProcessesInfo(); break;
                case "3": endProcess(); break;
                default: System.out.println("Данной операции нет, попробуйте снова");
            }
        }
    }
    private static void createProcess() {
        System.out.print("Введите имя процесса: ");
        String name = in.nextLine().trim();
        ProcessManager.createProcess(name);
    }
    private static void showProcessesInfo() {
        ProcessManager.showProcessesInfo();
    }
    private static void endProcess() {
        System.out.print("Введите имя процесса для завершения: ");
        String name = in.nextLine().trim();
        System.out.print("Завершить процесс? (да/нет): ");
        String confirm = in.nextLine().trim().toLowerCase();
        if (confirm.equals("да")) {
            ProcessManager.endProcess(name);
        }
        else {
            System.out.println("Отмена завершения");
        }
    }
}
