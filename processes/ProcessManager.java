import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ProcessManager {
    private static final List<String> processNames = new ArrayList<>();
    private static final List<ProcessHandle> processHandles = new ArrayList<>();
    public static void createProcess(String name) {
        if (name.isEmpty()) {
            System.out.println("Имя не введено");
            return;
        }
        String command = name.endsWith(".exe") ? name : name + ".exe";
        try {
            Process process = new ProcessBuilder(command).start();
            ProcessHandle handle = process.toHandle();
            processNames.add(command);
            processHandles.add(handle);
            System.out.println("Запущен процесс: " + command + " (PID - " + handle.pid() + ")");
        } catch (IOException e) {
            System.out.println("Не удалось запустить процесс: " + e.getMessage());
        }
    }
    public static void showProcessesInfo() {
        if (processNames.isEmpty()) {
            System.out.println("Нет активных процессов");
            return;
        }
        for (int i = 0; i < processNames.size(); i++) {
            ProcessHandle ph = processHandles.get(i);
            boolean alive = ph.isAlive();
            String status = alive ? "активен" : "завершён";
            System.out.println("№ " + (i + 1) + " Имя: " + processNames.get(i) + " PID: " + ph.pid() + " Статус: " + status);
            System.out.println(ph.info());
        }
    }
    public static void endProcess(String name) {
        String command = name.endsWith(".exe") ? name : name + ".exe";
        boolean found = false;
        for (int i = processNames.size() - 1; i >= 0; i--) {
            if (processNames.get(i).equals(command)) {
                found = true;
                ProcessHandle ph = processHandles.get(i);
                if (ph.isAlive()) {
                    boolean terminated = ph.destroy();
                    if (!terminated) {
                        ph.destroyForcibly();
                    }
                    System.out.println("Процесс " + command + " завершён");
                } else {
                    System.out.println("Процесс " + command + " уже завершён");
                }
                processNames.remove(i);
                processHandles.remove(i);
            }
        }
        if (!found) {
            System.out.println("Процесс с именем " + command + " не найден");
        }
    }
}
