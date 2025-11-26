import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadHelper {
    public static ExecutorService executor = Executors.newFixedThreadPool(5);

    // 1 - Скачать и открыть картинку
    public static void downloadAndOpenImage(Scanner scanner) {
        System.out.print("Введите URL картинки: ");
        String imageUrl = scanner.nextLine().trim();
        executor.submit(() -> {
            try {
                String fileName = Main.IMAGE_PATH + "image_" + System.currentTimeMillis() + getFileExtension(imageUrl);
                downloadFile(imageUrl, fileName);
                File imageFile = new File(fileName);
                System.out.println("Скачана картинка " + imageFile.getName());
                // Отображение скаченной картинки
                openImageFile(imageFile);
                System.out.println("Картинка открыта!");

            } catch (IOException e) {
                System.out.println("Ошибка загрузки картинки: " + e.getMessage());
            }
        });
    }

    // 2 - Скачать и включить музыку
    public static void downloadAndPlaySong(Scanner scanner) {
        System.out.print("Введите URL музыки: ");
        String songUrl = scanner.nextLine().trim();
        executor.submit(() -> {
            try {
                String fileName = Main.SONG_PATH + "track_" + System.currentTimeMillis() + getFileExtension(songUrl);
                downloadFile(songUrl, fileName);
                System.out.println("Музыка скачана: " + new File(fileName).getName());
                // Воспроизведение музыки
                playMusic(new File(fileName));
                System.out.println("Музыка запущена!");
            } catch (IOException e) {
                System.out.println("Ошибка загрузки музыки: " + e.getMessage());
            }
        });
    }

    // 3 - Проигрывать музыку параллельно скачивая картинку
    public static void parallelSongAndImage(Scanner scanner) {
        System.out.print("Введите URL музыки: ");
        String songUrl = scanner.nextLine().trim();
        System.out.print("Введите URL картинки: ");
        String imageUrl = scanner.nextLine().trim();
        final String songFileName = Main.SONG_PATH + "track_" + System.currentTimeMillis() + getFileExtension(songUrl);
        final String imageFileName = Main.IMAGE_PATH + "image_" + System.currentTimeMillis() + getFileExtension(imageUrl);
        // Загрузка музыки и воспроизведение
        executor.submit(() -> {
            try {
                downloadFile(songUrl, songFileName);
                System.out.println("Музыка скачана: " + new File(songFileName).getName());
                // Воспроизведение музыки после загрузки
                playMusic(new File(songFileName));
                System.out.println("Музыка запущена!");
            } catch (IOException e) {
                System.out.println("Ошибка загрузки музыки: " + e.getMessage());
            }
        });
        // Параллельная загрузка картинки
        executor.submit(() -> {
            try {
                downloadFile(imageUrl, imageFileName);
                System.out.println("Картинка скачана: " + new File(imageFileName).getName());
                openImageFile(new File(imageFileName));
                System.out.println("Картинка открыта!");
            } catch (IOException e) {
                System.out.println("Ошибка загрузки картинки: " + e.getMessage());
            }
        });
        System.out.println("Параллельная загрузка запущена...");
    }

    private static String getFileExtension(String url) {
        if (url.contains(".mp3")) return ".mp3";
        if (url.contains(".wav")) return ".wav";
        if (url.contains(".png")) return ".png";
        if (url.contains(".jpeg")) return ".jpeg";
        if (url.contains(".gif")) return ".gif";
        return ".jpg";
    }

    private static void downloadFile(String fileUrl, String fileName) throws IOException {
        URL url = new URL(fileUrl);
        try (ReadableByteChannel byteChannel = Channels.newChannel(url.openStream()); FileOutputStream stream = new FileOutputStream(fileName)) {
            stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        }
    }
    private static void playMusic(File musicFile) {
        try {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "\"\"", "\"" + musicFile.getAbsolutePath() + "\""});
        } catch (Exception e) {
            System.out.println("Ошибка запуска плеера: " + e.getMessage());
        }
    }

    private static void openImageFile(File imageFile) {
        try {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "\"\"", "\"" + imageFile.getAbsolutePath() + "\""});
        } catch (Exception e) {
            System.out.println("Ошибка открытия картинки: " + e.getMessage());
        }
    }
}
