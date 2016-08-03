import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVParser {

    static double[][] testData = { { 0.909508984 }, { 0.994146604 },
            { 0.547739112 }, { 0.526967837 }, { 0.536109471 }, { 0.623924133 },
            { 0.772965174 }, { 0.556421209 }, { 0.810262151 }, { 0.015512116 },
            { 0.566335453 }, { 0.290084722 }, { 0.71007006 }, { 0.180146901 },
            { 0.061236344 }, { 0.045359894 }, { 0.186120912 }, { 0.485233586 },
            { 0.939386933 }, { 0.386945026 }, { 0.59325852 }, { 0.761866556 },
            { 0.787883864 }, { 0.521404444 }, { 0.578701636 } };

    static double[][] testData2 = { { 1.0, 2.8, 2.6, 2.7 },
            { 2.0, 3.4, 3.2, 3.1 }, { 3.0, 4.2, 4.6, 4.1 },
            { 1.0, 9.2, 9.6, 9.1 } };

    static double[][] testData3 = { { 1.0, 2 }, { 2.0, 3 }, { 3.0, 4 },
            { 4.0, 5 } };

    public static List<List<Double>> readCSV(String fileName) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            line = br.readLine();
            while (line != null) {
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
            System.out.println(fileName + " doesn't exist");
            data = null;
        } catch (IOException e) {
            System.out.println("Invalid entry in the csv");
            data = null;
        } catch (NumberFormatException e) {
            System.out.println("Unable to parse the csv");
            data = null;
        }

        return data;

    }

    public static void main(String[] args) {
        List<List<Double>> list1 = new ArrayList<List<Double>>();
        List<List<Double>> list2 = new ArrayList<List<Double>>();

        for (int i = 0; i < testData2.length; i++) {
            List<Double> subList = new ArrayList<Double>();
            for (int j = 0; j < testData2[i].length; j++) {
                subList.add(testData2[i][j]);
            }
            list1.add(subList);
        }

        for (int i = 0; i < testData3.length; i++) {
            List<Double> subList = new ArrayList<Double>();
            for (int j = 0; j < testData3[i].length; j++) {
                subList.add(testData3[i][j]);
            }
            list2.add(subList);
        }

        long time = System.nanoTime();

        // list1 = readCSV("Book1.csv");
        if (list1 != null) {
            CSVData data1 = new CSVData(list1);
            CSVData data2 = new CSVData(list2);
            System.out.println(data1.findMax(0));
            System.out.println(data1.findMin(0));
            System.out.println(data1.findMean(0));
            System.out.println(data1.findMedian(0));

            System.out.println((System.nanoTime() - time) / 1000000 + " ms");

            List<Double> ret = data1.div(0, 1);
            for (int i = 0; i < ret.size(); i++) {
                System.out.println(ret.get(i));
            }

            CSVData data3 = data1.joins(0, data2, 0, true);
            System.out.println(data3.toString());
        }

    }

}
