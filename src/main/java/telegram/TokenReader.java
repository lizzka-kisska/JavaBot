package telegram;

import java.io.FileReader;
import java.util.Scanner;

public class TokenReader {
    public String tokenReader(String file) throws Exception {
        FileReader f = new FileReader(file);
        Scanner scan = new Scanner(f);

        String token = scan.nextLine();

        f.close();

        return token;
    }
}
