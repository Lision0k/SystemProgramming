import java.io.*;
import java.util.Scanner;

/** В директории, указанной пользователем, обрабатывает файл с расширением .java. Лаунчер обфускатора*/
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите путь к директории с файлами: ");
        String dirPath = in.next();
        File dir = new File(dirPath);
        File[] javaFiles = dir.listFiles((d, name) -> name.endsWith(".java"));
        if (javaFiles == null || javaFiles.length == 0) {
            System.out.println("В директории не найдено файлов с расширением .java");
            return;
        }
        System.out.print("\nВыберите файл для обработки (введите номер или имя файла): ");
        String select = in.nextLine().trim();
        File selectedFile = selectFile(select, javaFiles);
        if (selectedFile == null) {
            System.out.println("Ошибка. Файла с таким именем или номером не существует");
            return;
        }
        ObfuscateHelper.obfuscateJavaFile(selectedFile);
        System.out.printf("Обфускация кода в файле %S прошла успешно!", selectedFile.getName());
    }

    private static File selectFile(String input, File[] javaFiles) {
        try{
            //Если введен номер
            int fileNumber = Integer.parseInt(input);
            if (fileNumber >= 1 && fileNumber < javaFiles.length) {
                return javaFiles[fileNumber];
            }
        } catch (Exception e) {
            //Если введено имя файла
            for (File file : javaFiles) {
                if (file.getName().toString().equals(input)) {
                    return file;
                }
            }
        }
        return null;
    }
}
