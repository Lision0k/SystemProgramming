import java.io.*;
import java.nio.channels.FileChannel;

class CopyingFiles {
    static File createFileObject(String name) {
        File f = new File(name);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Error of creating");
            }
        }
        return f;
    }
}
class NIOCopying{
    /** метод для последовательного копирования файлов **/
    static void consNIOCopy(File src, File dst){
        // Получаем FileChannel исходного файла и целевого файла
        try(FileChannel srcFileChannel  = new FileInputStream(src).getChannel();
            FileChannel dstFileChannel = new FileOutputStream(dst).getChannel()){
            // Размер текущего FileChannel
            long count = srcFileChannel.size();
            while(count > 0){
                /**
                 * Записать байты из FileChannel исходного файла в целевой FileChannel
                 * srcFileChannel.position (): начальная позиция в исходном файле не может быть отрицательной
                 * count: максимальное количество переданных байтов, не может быть отрицательным
                 * dstFileChannel: целевой файл
                 **/
                long transferred = srcFileChannel.transferTo(srcFileChannel.position(),
                        count, dstFileChannel);
                // После завершения переноса измените положение исходного файла на новое место
                srcFileChannel.position(srcFileChannel.position() + transferred);
                // Рассчитаем, сколько байтов осталось перенести
                count -= transferred;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** метод для параллельного копирования файлов **/
    static void parallNIOCopy(File src, File dst1, File dst2){
        // Получаем FileChannel исходного файла и целевого файла
        try(FileChannel srcFileChannel  = new FileInputStream(src).getChannel();
            FileChannel dst1FileChannel = new FileOutputStream(dst1).getChannel();
            FileChannel dst2FileChannel = new FileOutputStream(dst2).getChannel()){
            // Размер текущего FileChannel
            long count = srcFileChannel.size();
            while(count > 0){
                /**
                 * Записываем байты из FileChannel исходного файла в целевой FileChannel
                 * srcFileChannel.position (): начальная позиция в исходном файле не может быть отрицательной
                 * count: максимальное количество переданных байтов, не может быть отрицательным
                 * dstFileChannel: целевой файл
                 **/
                srcFileChannel.transferTo(srcFileChannel.position(),
                        count, dst1FileChannel);
                long transferred = srcFileChannel.transferTo(srcFileChannel.position(),
                        count, dst2FileChannel);
                // После завершения переноса изменяем положение исходного файла на новое место
                srcFileChannel.position(srcFileChannel.position() + transferred);
                // Рассчитываем, сколько байт осталось перенести
                count -= transferred;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        File source = CopyingFiles.createFileObject("text.txt");
        File dest1 = CopyingFiles.createFileObject("textcopy1.txt");
        File dest2 = CopyingFiles.createFileObject("textcopy2.txt");
      
        //последовательное копирование
        long time = System.nanoTime();
        NIOCopying.consNIOCopy(source, dest1);
        NIOCopying.consNIOCopy(source, dest2);
        System.out.print("Время последовательного копирования: ");
        System.out.println(System.nanoTime() - time);

        //параллельное копирование
        time = System.nanoTime();
        NIOCopying.parallNIOCopy(source, dest1, dest2);
        System.out.print("Время параллельного копирования: ");
        System.out.println(System.nanoTime() - time);
    }
}
