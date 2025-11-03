public class ConverterException extends Exception{
    public ConverterException(String message){
        super(message);
    }
    /** Метод проверяет соответствует ли строка числу, если нет, то вызывается кастомное исключение
     * @param input введенная строка */
    public static void ValidCheck(String input) throws ConverterException{
        if(!input.matches("\\d+")){
            throw new ConverterException("Ошибка. Введённые данные не являютя числом ");
        }
        else{
            System.out.println("Ок");
        }
    }
}
