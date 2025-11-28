import java.io.File;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/** Подзадачи:
 Сжать код за счет удаления лишних пробелов и символов перехода на новую строку
 Сжать код за счет удаления комментариев
 Заменить имя класса, а также соответственно имя файла и имена конструкторов
 Заменить идентификаторы на односимвольные или двухсимвольные имена в зависимости от количества найденных идентификаторов в коде
 */
public class ObfuscateHelper {
    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected static int counter = -1;

    /**
     * Основной метод для обфускации файла
     */
    public static void obfuscateJavaFile(File javaFile) {
        try {
            //Читаем исходный файл в строку
            String content = readFile(javaFile);
            String className = javaFile.getName().substring(0, javaFile.getName().indexOf(".java"));
            //Удаляем комментарии
            content = removeComments(content);
            //Сжимаем код (удаляем лишние пробелы и переводы строк)
            content = compressCode(content);
            //Заменяем идентификаторы
            content = replaceIdentifiers(content);
            //Генерируем новое имя класса
            String newClassName = generateNewName();
            //Заменяем имя класса, конструкторов и имя файла
            content = replaceClassName(content, className, newClassName);
            //Записываем обфусцированный код обратно в файл
            writeFile(javaFile, content);
            //Переименовываем файл в соответствии с новым именем класса
            renameFile(javaFile, newClassName);
        } catch (IOException e) {
            System.err.println("Ошибка при обфускации файла: " + e.getMessage());
        }
    }

    /**
     * Чтение файла в строку
     */
    private static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /**
     * Запись строки в файл
     */
    private static void writeFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    /**
     * Удаление комментариев из кода
     */
    private static String removeComments(String content) {
        // Удаляем многострочные комментарии /* */
        content = content.replaceAll("/\\*.*?\\*/", "");
        // Удаляем однострочные комментарии //
        content = content.replaceAll("//.*", "");
        return content;
    }

    /**
     * Сжатие кода - удаление лишних пробелов и переводов строк
     */
    private static String compressCode(String content) {
        // Заменяем множественные пробелы на один
        content = content.replaceAll("\\s+", " ");
        // Удаляем пробелы вокруг некоторых символов
        content = content.replaceAll("\\s*([{}();,=])\\s*", "$1");
        return content.trim();
    }

    /**
     * Замена идентификаторов на короткие имена
     */
    private static String replaceIdentifiers(String content) {
        // Собираем все идентификаторы (переменные, методы)
        Set<String> identifiers = new HashSet<>();
        // Ищем идентификаторы переменных и методов
        Pattern pattern = Pattern.compile("\\b(?!class|public|private|protected|static|final|void|int|String|boolean|if|else|for|while|return|new|this|extends|implements)([a-zA-Z_][a-zA-Z0-9_]*)\\b");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String identifier = matcher.group(1);
            if (identifier.length() > 2) { // Заменяем только длинные идентификаторы
                identifiers.add(identifier);
            }
        }
        // Генерируем замены
        Map<String, String> replacements = new HashMap<>();
        for (String identifier : identifiers) {
            String newName = generateNewName();
            replacements.put(identifier, newName);
        }
        // Заменяем идентификаторы в коде
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            content = content.replaceAll("\\b" + Pattern.quote(entry.getKey()) + "\\b", entry.getValue());
        }
        return content;
    }

    /**
     * Генерация нового имени
     */
    private static String generateNewName() {
        counter++;
        if (counter < chars.length()) {
            return String.valueOf(chars.charAt(counter));
        } else {
            int firstChar = counter / chars.length() - 1;
            int secondChar = counter % chars.length();
            return String.valueOf(chars.charAt(firstChar)) + chars.charAt(secondChar);
        }
    }

    /**
     * Замена имени класса и конструкторов
     */
    private static String replaceClassName(String content, String oldName, String newName) {
        // Заменяем объявление класса
        content = content.replaceAll("\\bclass\\s+" + oldName + "\\b", "class " + newName);
        // Заменяем конструкторы
        content = content.replaceAll("\\b" + oldName + "\\s*\\(", newName + "(");
        // Заменяем использование в выражениях new
        content = content.replaceAll("\\bnew\\s+" + oldName + "\\b", "new " + newName);
        return content;
    }

    /**
     * Переименование файла
     */
    private static void renameFile(File javaFile, String newClassName) {
        File newFile = new File(javaFile.getParent(), newClassName + ".java");
        if (!javaFile.renameTo(newFile)) {
            System.err.println("Не удалось переименовать файл " + javaFile.getName() + " в " + newFile.getName());
        }
    }
}
