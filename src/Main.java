import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        String choice = "";
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("Dusche oder Bad? (dusche/bad): ");
            choice = scanner.nextLine(). toLowerCase();
            if (choice.equals("dusche") || choice.equals("bad")){
                validChoice = true;
            } else {
                System.out.println("Bitte gid 'dusche' oder 'bad' en.");
            }
        }
        int water = 0;
        if (choice.equals("dusche")) {

            int minutes = 0;
            boolean validInput = false;

            while (!validInput) {
                System.out.print("Wie viele Minuten duschst du pro Tag? ");
                String input = scanner.nextLine();

                try {
                    minutes = Integer.parseInt(input);
                    if (minutes > 0) {
                        validInput = true;
                    } else {
                        System.out.println("Bitte gib eine Zahl größer als 0 ein.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Das ist kein Zahl. Bitte versuche es erneut.");
                }
            }
            water = minutes * 12;
        }
        else {
            water = 120;
        }
        double temperatureDiff = 30; // 40 - 10
        double kwh = water * 0.001163 * temperatureDiff;

        int optimalWater = 5 * 12;
        int saving = water - optimalWater;

        System.out.println("\n--- Dein Wasserbereicht ---\n");
        System.out.println("Art: " +choice);
        System.out.println("Wasserverbrauch: " + water + "Liter");
        System.out.printf("Energieverbrauch: %.2f kWh%n%n", kwh);

        System.out.println("Bewertung:");
        if (choice.equals("dusche")){
            System.out.println("Du verbrauchst ca. " + water + " Liter Wasser pro Dusche.");
        } else {
            System.out.println("Du verbrauchst ca. " + water + " Liter Wasser pro Bad.");
        }

        if (water <= 60) {
            System.out.println("Sehr gut! Dein Wasserverbrauch ist niedrig.");
        } else if (water <= 100) {
            System.out.println("Dein Wasserverbrauch ist normal.");
        } else if (water <= 150) {
            if (choice.equals("dusche")) {
                System.out.println("Du verbrauchst viel Wasser. Vielleicht kürzer duschen?");
            } else {
                System.out.println("Du verbrauchst viel Wasser. Ein Bad Verbraucht viel Wasser.");
            }
        } else {
            System.out.println("Sehr hoher Wasserverbrauch  Bitte spare Wasser!");
        }

        if (choice.equals("dusche") && saving > 0){
            System.out.println("Wenn du nur 5 Minuten duschst, kannst du "
                    + saving + " Liter Wasser sparen");
        }
        boolean askDishes = true;
        String dishesChoice = "";

        while (askDishes) {
            System.out.print("Möchtest du wissen, wie viel Wasser fürs Geschirrspülen verbraucht wird? (ja/nein): ");
            dishesChoice = scanner.nextLine().toLowerCase();

            if (dishesChoice.equals("ja")) {
                System.out.println("Beim Geschirrspülen werden im Durchschnitt ca. 40 Liter Wasser verbraucht.");
                askDishes = false;
            } else if (dishesChoice.equals("nein")) {
                System.out.println("Alles klar. Tschüss ");
                askDishes = false;
            } else {
                System.out.println("Bitte gib nur 'ja' oder 'nein' ein.");
            }
        }

    }
}