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

    static double[] testData2 = {};

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
        List<List<Double>> list = new ArrayList<List<Double>>();
        /*
         * for(int i=0;i<testData.length;i++) { List<Double> subList = new
         * ArrayList<Double>(); for(int j=0;j<testData[i].length;j++) {
         * subList.add(testData[i][j]); } list.add(subList); }
         */

        long time = System.nanoTime();

        list = readCSV("Book1.csv");
        if (list != null) {
            CSVData data = new CSVData(list);
            System.out.println(data.findMax(0));
            System.out.println(data.findMin(0));
            System.out.println(data.findMean(0));
            System.out.println(data.findMedian(0));

            System.out.println((System.nanoTime() - time) / 1000000 + " ms");

            List<Double> ret = data.div(0, 1);
            for (int i = 0; i < ret.size(); i++) {
                System.out.println(ret.get(i));
            }

        }

    }

}
