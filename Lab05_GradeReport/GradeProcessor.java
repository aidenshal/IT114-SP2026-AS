
public class GradeProcessor {

    public static void main(String[] args) {
        
        // Input and Output file names
        String inputFileName = "student_data.txt";
        String outputFileName = "grade_report.txt";

        // TODO 1: Wrap your code in a try-catch block.
        // The catch block should handle 'FileNotFoundException'.

            // TODO 2: Create a File object and a Scanner to read the input file.
            
            // TODO 3: Create a PrintWriter object to write to the output file.
            
            System.out.println("Processing file...");
            
            // Write the header to the output file separated by tab characters (\t);
            // Print a dashed line ("--------------------");

            // TODO 4: Create a while loop to process the file line by line.
            //   - Read the name (String)
            //   - Read the three scores (int or double)
            //   - Calculate the average
            //   - Determine "Pass" or "Fail" (Pass is >= 70.0)
            //   - Write the formatted line to the output file

            // TODO 5: Close both the Scanner and the PrintWriter.
            
            System.out.println("Done! Check " + outputFileName + " for results.");

        // TODO 6: catch block:
    }
}