import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** Текстовый файл состоит не более чем из 106 символов арабских цифр (0,1,...,9).
 * Определите максимальное количество идущих подряд цифр, среди которых сумма двух идущих подряд цифр больше цифры следующей за ними
 * */
public class NumsFile {
    File f;

    NumsFile(File f){
        this.f = f;
    }

    public int CountNums(){
        int counter = 2, maxcounter = 2;
        try(FileReader reader = new FileReader(f))
        {
            StringBuilder builder = new StringBuilder();
            char[] buf = new char[256];
            int read;
            while ((read = reader.read(buf)) != -1) {
                builder.append(new String(buf, 0, read));
            }
            int first, second, third;
            for (int i = 0; i < builder.length()-2; i++) {
                first = builder.charAt(i) - 48;
                second = builder.charAt(i + 1) - 48;
                third = builder.charAt(i + 2) - 48;
                if((first + second) > third){
                    counter ++;
                }
                else
                    counter = 2;
                if(counter > maxcounter)
                    maxcounter = counter;
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }

        return maxcounter;
    }
}
