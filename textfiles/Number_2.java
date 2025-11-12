import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** Текстовый файл состоит не более чем из 10^6 символов X, Y и Z.
 * Определите длину самой длинной последовательности, состоящей из символов X.
 * Хотя бы один символ X находится в последовательности
 * */
public class XYZFile {
    File f;

    XYZFile(File f){
        this.f = f;
    }

    public int XSeria(){
        int maxcount = 0, count = 0, s;
        try(FileReader reader = new FileReader(f)){
            while((s = reader.read()) != -1){
                if(s == 88)
                    count++;
                else
                    count = 0;
                if(count > maxcount)
                    maxcount = count;
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return maxcount;
    }
}
