import java.util.ArrayList;
import java.util.Scanner;

public class ContactManager {

    // --- REGEX HELPER METHOD ---
    public static String formatPhoneNumber(String raw) {
        if (raw == null) return "Invalid Number";

        // Step A: Strip non-digits
        String digitsOnly = raw.replaceAll("\\D", "");

        // Step B: Check length and format as XXX-XXX-XXXX
        if (digitsOnly.length() == 10) {
            return digitsOnly.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        }

        return "Invalid Number";
    }

    public static void main(String[] args) {
        ArrayList<Contact> contacts = new ArrayList<>();

        // Contacts to be sanitized and sorted
        contacts.add(new Contact("Zack Morris", "zack@bayside.edu", "555.123.4567"));
        contacts.add(new Contact("Alice Smith", "alice@test.com", "(555) 999-8888"));
        contacts.add(new Contact("Bob Jones", "bob@test.com", "5551112222"));

        System.out.println("--- Cleaning Data ---");

        // CLEAN DATA (Loop and Format)
        for (Contact c : contacts) {
            c.setPhoneNumber(formatPhoneNumber(c.getPhoneNumber()));
        }

        System.out.println("--- Sorting Data ---");

        // SORT DATA (Bubble Sort by name)
        for (int i = 0; i < contacts.size() - 1; i++) {
            for (int j = 0; j < contacts.size() - 1 - i; j++) {
                String name1 = contacts.get(j).getName();
                String name2 = contacts.get(j + 1).getName();

                if (name1.compareTo(name2) > 0) {
                    Contact temp = contacts.get(j);
                    contacts.set(j, contacts.get(j + 1));
                    contacts.set(j + 1, temp);
                }
            }
        }

        // Print sorted list
        for (Contact c : contacts) {
            System.out.println(c);
        }

        // Input name to search for
        System.out.println("\n--- Search ---");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a name to find: ");
        String searchName = scan.nextLine();

        boolean found = false;

        // LINEAR SEARCH
        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(searchName.trim())) {
                System.out.println("FOUND: Name: " + c.getName() + " | Phone: " + c.getPhoneNumber());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Contact not found.");
        }

        scan.close();
    }
}