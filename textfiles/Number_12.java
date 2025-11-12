import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** Текстовый файл содержит только заглавные буквы латинского алфавита (ABC…Z).
 * Определите символ, который чаще всего встречается в файле между двумя одинаковыми символами.
 Например, в тексте CBCABABACCC есть комбинации CBC, ABA (два раза), BAB и CCC.
 Чаще всего — 3 раза — между двумя одинаковыми символами стоит B, в ответе для этого случая надо написать B
 * */
public class ABCZFile {
    File f;

    ABCZFile(File f){
        this.f = f;
    }

    public char InCombinations(){
        int[] codes = new int[26];
        try(FileReader reader = new FileReader(f))
        {
            StringBuilder builder = new StringBuilder();
            char[] buf = new char[256];
            int read;
            while ((read = reader.read(buf)) != -1) {
                builder.append(new String(buf, 0, read));
            }
            for(int i = 0; i < builder.length()-2; i++){
                if(builder.charAt(i) == builder.charAt(i+2)) {
                    int index = builder.charAt(i+1) - 65;
                    codes[index]++;
                }
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
        int max = 0;
        for(int i = 1; i < 26; i++){
            if(codes[i] > codes[max])
                max = i;
        }
        return (char) (max + 65);
    }
}
