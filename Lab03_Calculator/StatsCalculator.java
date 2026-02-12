public class StatsCalculator {

    public static void main(String[] args) {
        // 1. Initialize the 2D array with the sample data
        double[][] scores = {
            {85.5, 90.0, 78.5, 92.0},
            {76.0, 88.5, 90.0, 85.0},
            {95.0, 92.0, 94.5, 98.0},
            {60.0, 70.5, 65.0, 72.0},
            {82.0, 84.0, 80.0, 88.0}
        };

        // 2. Call methods
        System.out.println("--- GradeBook Statistics ---\n");
        calculateStudentAverages(scores);
        System.out.println();
        calculateAssignmentAverages(scores);
        System.out.println();
        findHighestScore(scores);
    }

    // Row-major traversal: each row = one student
    public static void calculateStudentAverages(double[][] data) {
        System.out.println("Student Averages:");

        for (int r = 0; r < data.length; r++) {
            double sum = 0.0;

            for (int c = 0; c < data[r].length; c++) {
                sum += data[r][c];
            }

            double avg = sum / data[r].length;
            System.out.printf("Student %d: %.2f%n", (r + 1), avg);
        }
    }

    // Column-major traversal: each column = one assignment
    public static void calculateAssignmentAverages(double[][] data) {
        System.out.println("Assignment Averages:");

        int numStudents = data.length;
        int numAssignments = data[0].length;

        for (int c = 0; c < numAssignments; c++) {
            double sum = 0.0;

            for (int r = 0; r < numStudents; r++) {
                sum += data[r][c];
            }

            double avg = sum / numStudents;
            System.out.printf("Assignment %d: %.2f%n", (c + 1), avg);
        }
    }

    // Search entire 2D array for the max score
    public static void findHighestScore(double[][] data) {
        double max = data[0][0];
        int maxStudent = 0;
        int maxAssignment = 0;

        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[r].length; c++) {
                if (data[r][c] > max) {
                    max = data[r][c];
                    maxStudent = r;
                    maxAssignment = c;
                }
            }
        }

        System.out.printf(
            "Highest Score in Class: %.2f (Student %d, Assignment %d)%n",
            max, (maxStudent + 1), (maxAssignment + 1)
        );
    }
}
