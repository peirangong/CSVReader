import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* Peiran Gong 8/4/2016 */

public class CSVParser {

    /* Help section */
    private static void printHelp() {
        System.out.println("Usage: java CSVParser [Options]");
        System.out
                .println("-h --help                                                                : display this help menu");
        System.out
                .println("-d --display <.csv file> <col number1> <col number2> ...                 : display the given column(s)");
        System.out
                .println("-s --stat <.csv file> <col number>                                       : display the stats (max, min, mean, median) of the given column from the dataset");
        System.out
                .println("-c --calculation <.csv file> <col number> <col number> <add/sub/mul/div> : performs +,-,*,/ on the two columns");
        System.out
                .println("-i --inner <.csv file> <col number> <.csv file> <col number>             : inner join of the two given datasets with the two columns");
        System.out
                .println("-o --outer <.csv file> <col number> <.csv file> <col number>             : full outer join of the two given datasets with the two columns");
    }

    private static void printInvalidInput() {
        System.out.println("Invalid input");
    }

    /* Opens the file and parses input csv file */
    private static CSVData readCSV(String fileName) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            line = br.readLine();
            while (line != null) {
                // Split the data by comma and parse each entry as double
                String[] split = line.split(",");
                List<Double> list = new ArrayList<Double>();
                for (int i = 0; i < split.length; i++) {
                    list.add(Double.parseDouble(split[i]));
                }
                data.add(list);
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Csv file " + fileName + " doesn't exist");
            data = null;
        } catch (IOException e) {
            System.out.println("Invalid entry in the csv");
            data = null;
        } catch (NumberFormatException e) {
            System.out.println("Unable to parse the csv");
            data = null;
        }

        return new CSVData(data);
    }

    /* Performs inner/outer join on the input data */
    private static void join(String[] args) {
        if (args.length != 5) {
            throw new IllegalArgumentException();
        } else {
            String fileName1 = args[1];
            int col1 = Integer.parseInt(args[2]);
            String fileName2 = args[3];
            int col2 = Integer.parseInt(args[4]);
            boolean isOuter = args[0].equals("-o") || args[0].equals("--outer");

            CSVData inputData1 = readCSV(fileName1);
            CSVData inputData2 = readCSV(fileName2);
            CSVData outputData = CSVData.joins(inputData1, col1, inputData2,
                    col2, isOuter);

            System.out.println(outputData);
        }
    }

    /* Performs +, -, *, / operations on the input two columns */
    private static void calculation(String[] args) {
        if (args.length != 5) {
            throw new IllegalArgumentException();
        } else {
            String fileName = args[1];
            int col1 = Integer.parseInt(args[2]);
            int col2 = Integer.parseInt(args[3]);
            String op = args[4];

            CSVData inputData = readCSV(fileName);
            CSVData outputData = new CSVData();

            if (op.equals("add")) {
                outputData = inputData.add(col1, col2);
            } else if (op.equals("sub")) {
                outputData = inputData.sub(col1, col2);
            } else if (op.equals("mul")) {
                outputData = inputData.mul(col1, col2);
            } else if (op.equals("div")) {
                outputData = inputData.div(col1, col2);
            } else {
                throw new IllegalArgumentException();
            }

            System.out.println(outputData.toString());
        }
    }

    /* Displays the stats of the input column */
    private static void displayStats(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException();
        } else {
            String fileName = args[1];
            int col = Integer.parseInt(args[2]);

            CSVData inputData = readCSV(fileName);
            List<Double> stats = inputData.getStats(col);

            String[] text = { "Min: ", "Max: ", "Median: ",
                    "Mean: " };

            if (stats.size() == 0) {
                throw new IllegalArgumentException();
            } else {
                for (int i = 0; i < stats.size(); i++) {
                    System.out.println(text[i] + stats.get(i));
                }
            }
        }
    }

    /* Display the input column(s) */
    private static void displayColumns(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException();
        } else {
            String fileName = args[1];
            int[] cols = new int[args.length - 2];

            CSVData inputData = readCSV(fileName);
            for (int i = 0; i < cols.length; i++) {
                cols[i] = Integer.parseInt(args[i + 2]);
            }
            List<List<Double>> data = inputData.getCols(cols);

            CSVData outputData = new CSVData(data);
            System.out.println(outputData.toString());
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length == 0 || args[0].equals("-h")
                    || args[0].equals("--help")) {
                printHelp();
            } else if (args.length > 0) {
                if (args[0].equals("-d") || args[0].equals("--display")) {
                    // Display input cols from csv file
                    displayColumns(args);
                } else if (args[0].equals("-s") || args[0].equals("--stat")) {
                    // Display the stats of the input col
                    displayStats(args);
                } else if (args[0].equals("-c")
                        || args[0].equals("--calculation")) {
                    // Display calculation result of the two input columns
                    calculation(args);
                } else if (args[0].equals("-i") || args[0].equals("--inner")
                        || args[0].equals("-o") || args[0].equals("--outer")) {
                    // Display the inner/outer joins based on input columns
                    join(args);
                }
            }
        } catch (Exception e) {
            printInvalidInput();
            printHelp();
        }
    }
}
