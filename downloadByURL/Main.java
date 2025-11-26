import java.io.*;
import java.util.Scanner;

/** Лаунчер */
public class Main {
    public static final String IMAGE_PATH = "images/";
    public static final String SONG_PATH= "music/";

    public static void main(String[] args) {
        try {
            new File(SONG_PATH).mkdirs();
            new File(IMAGE_PATH).mkdirs();
            Scanner scanner = new Scanner(System.in);
            boolean bool = true;
            while (bool) {
                System.out.println("1 - Скачать и открыть картинку");
                System.out.println("2 - Скачать и включить музыку");
                System.out.println("3 - Проигрывать музыку параллельно скачивая картинку");
                System.out.println("4 - Завершить программу");
                String cases = scanner.nextLine();
                switch (cases) {
                    case "1": DownloadHelper.downloadAndOpenImage(scanner);break;
                    case "2": DownloadHelper.downloadAndPlaySong(scanner);break;
                    case "3": DownloadHelper.parallelSongAndImage(scanner);break;
                    case "4": bool = false; break;
                    default: System.out.println("Такой операции нет");
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            DownloadHelper.executor.shutdown();
        }
    }
}
