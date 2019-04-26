public class Main {

    public static void main(String[] args) {
        java.util.Scanner leitor = new java.util.Scanner(System.in);

        Scanner scanner = new Scanner(leitor.nextLine());

        Token t;

        do {
            t = scanner.nextToken();

            System.out.println(t);
        } while (t.getName() != Names.EOF);
    }
}
