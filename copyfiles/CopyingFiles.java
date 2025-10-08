import java.io.*;
class CopyingFiles {
    static File createFile(String name){
        File f = new File(name);
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Error of creating");
            }
        }
        return f;
    }

    static void consCopy(File f, File f1){
        InputStream in;
        OutputStream out;
        try{
            in = new FileInputStream(f);
            out = new FileOutputStream(f1);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }catch (IOException ex) {
            System.out.println("Error");
        }
    }

    static void parallCopy(File f, File f1, File f2){
        InputStream in;
        OutputStream out1;
        OutputStream out2;
        try{
            in = new FileInputStream(f);
            out1 = new FileOutputStream(f1);
            out2 = new FileOutputStream(f2);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out1.write(buffer, 0, length);
                out2.write(buffer, 0, length);
            }
            in.close();
            out1.close();
            out2.close();
        }catch (IOException ex) {
            System.out.println("Error");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        File source = CopyingFiles.createFile("text.txt");
        File dest1 = CopyingFiles.createFile("textcopy1.txt");
        File dest2 = CopyingFiles.createFile("textcopy2.txt");

        //последовательное копирование
        long time = System.nanoTime();
        CopyingFiles.consCopy(source, dest1);
        CopyingFiles.consCopy(source, dest2);
        System.out.print("Время последовательного копирования: ");
        System.out.println(System.nanoTime() - time);

        //параллельное копирование
        time = System.nanoTime();
        CopyingFiles.parallCopy(source, dest1, dest2);
        System.out.print("Время параллельного копирования: ");
        System.out.println(System.nanoTime() - time);
    }
}
