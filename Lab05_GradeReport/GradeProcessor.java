import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GradeProcessor {

    public static void main(String[] args) {

        // Input and Output file names
        String inputFileName = "student_data.txt";
        String outputFileName = "grade_report.txt";

        try {

            // Create File and Scanner to read input file
            File inputFile = new File(inputFileName);
            Scanner fileScanner = new Scanner(inputFile);

            // Create PrintWriter for output file
            PrintWriter writer = new PrintWriter(outputFileName);

            System.out.println("Processing file...");

            // Header
            writer.println("Name\tAverage\tStatus");
            writer.println("--------------------------------");

            // Process each line of the file
            while (fileScanner.hasNext()) {

                String name = fileScanner.next();
                double score1 = fileScanner.nextDouble();
                double score2 = fileScanner.nextDouble();
                double score3 = fileScanner.nextDouble();

                // Calculate average
                double average = (score1 + score2 + score3) / 3;

                // Determine pass/fail
                String status;
                if (average >= 70) {
                    status = "Pass";
                } else {
                    status = "Fail";
                }

                // Write to output file
                writer.printf("%s\t%.1f\t%s\n", name, average, status);
            }

            // Close resources
            fileScanner.close();
            writer.close();

            System.out.println("Done! Check " + outputFileName + " for results.");

        } catch (FileNotFoundException e) {

            System.out.println("Error: Input file 'student_data.txt' was not found.");

        }
    }
}