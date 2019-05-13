import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String input = "";
        FileReader fileReader = new FileReader("code.xpp");

        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                input += (line + "\n");
            }
        }

        Scanner scanner = new Scanner(input);

        Token t;

        do {
            t = scanner.nextToken();
            System.out.println(t);
        } while (t.getName() != Names.EOF);
    }
}
