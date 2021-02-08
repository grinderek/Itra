import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.Mac;

public class RockPaperScissors {

    private static void error(String type) {
        System.out.println("Input error - " + type + "\nRight input for example: rock paper scissors");
        System.exit(0);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        int len = args.length;

        if (len < 3) error("Not enough elements");
        if (len % 2 == 0) error("Not odd number of elements");
        for (int i = 0; i < len - 1; i++)
            for (int j = i + 1; j < len; j++)
                if (args[i].equals(args[j]))
                    error("Repeating elements");

        SecureRandom random = new SecureRandom();
        byte key[] = new byte[16];
        random.nextBytes(key);
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));

        int computer_number = random.nextInt(len - 1) + 1;
        String computer_choice = args[computer_number];
        byte[] bytes = computer_choice.getBytes();
        byte[] HMAC = mac.doFinal(bytes);
        System.out.println("HMAC: " + bytesToHex(HMAC));

        int my_number;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Available moves:");
            for (int i = 0; i < len; i++)
                System.out.println((i + 1) + " - " + args[i]);
            System.out.println("0 - exit");
            System.out.print("Enter your move: ");
            my_number = in.nextInt();
        }while (my_number > len);

        if (my_number == 0)
        {
            System.out.println("Goodbye!");
            System.exit(0);
        }


        my_number--;
        String my_choice = args[my_number];
        System.out.println("Your move: " + my_choice);
        System.out.println("Computer move: " + computer_choice);

        if (computer_number == my_number) {
            System.out.println("This is draw");
        }
        else
        if (computer_number + (len - 1) / 2 >= len) {
            if (computer_number < my_number || (computer_number + (len - 1) / 2) % len >= my_number)
                System.out.println("You lose!");
            else System.out.println("You win!");
        }
        else {
            if (computer_number < my_number && my_number <= computer_number + (len - 1) / 2)
                System.out.println("You lose!");
            else System.out.println("You win");
        }

        System.out.println("HMAC key: " + bytesToHex(key));
    }
}

