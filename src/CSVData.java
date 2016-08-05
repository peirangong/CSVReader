import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CSVData {
    public List<List<Double>> rawData;
    public int row;

    /* Constructor */
    public CSVData(List<List<Double>> data) {
        rawData = data;
        row = data.size();
    }

    /* Constructor */
    public CSVData() {
        rawData = null;
        row = 0;
    }

    /* Override the toString method to print data row by row */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (rawData == null || rawData.size() == 0) {
            return "Data is empty";
        }
        for (int i = 0; i < row; i++) {
            sb.append(rawData.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /* Return the input columns. Will skip columns that are out of range */
    public List<List<Double>> getCols(int[] cols) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (int i = 0; i < this.row; i++) {
            List<Double> line = new ArrayList<Double>();
            for(int j=0;j<cols.length;j++) {
                if (cols[j] < this.rawData.get(i).size()) {
                    line.add(this.rawData.get(i).get(cols[j]));
                }
            }

            /* If all input cols have null data, then it will skip */
            if (line.size() > 0) {
                data.add(line);
            }
        }
        return data;
    }

    /* Returns the min, max, median, and mean of the input column */
    public List<Double> getStats(int col) {
        List<Double> stats = new ArrayList<Double>();
        if (rawData == null || rawData.get(0).size() <= col) {
            return stats;
        }
        stats.add(this.findMin(col));
        stats.add(this.findMax(col));
        stats.add(this.findMedian(col));
        stats.add(this.findMean(col));

        return stats;
    }

    /* Returns the min value of the given column */
    private double findMin(int c) {
        double min = Double.MAX_VALUE;
        for(int i=0;i<row;i++) {
            min = Math.min(min, rawData.get(i).get(c));
        }
        return min;
    }

    /* Returns the max value of the given column */
    private double findMax(int c) {
        double max = Double.MIN_VALUE;
        for(int i=0;i<row;i++) {
            max = Math.max(max, rawData.get(i).get(c));
        }
        return max;
    }

    /* Returns the mean value of the given column */
    private double findMean(int c) {
        double mean = 0;
        for(int i=0;i<row;i++) {
            // Calculating the sum and then divide by number of rows could result overflow if the sum is too big
            mean = (mean * i + rawData.get(i).get(c)) / (i + 1);
        }
        return mean;
    }

    /*
     * Returns the median value of the given column. Uses quick select algorithm
     * to find the median
     */
    private double findMedian(int c) {
        double[] data = new double[row];
        double median = 0;
        for (int i = 0; i < data.length; i++) {
            // Fills the data into a local array
            data[i] = rawData.get(i).get(c);
        }
        // Shuffle the data to prevent worst case
        shuffle(data);

        int lo = 0;
        int hi = row - 1;
        while (lo < hi) {
            int j = partition(data, lo, hi);
            if (j < row / 2) {
                lo = j + 1;
            } else if (j > row / 2) {
                hi = j - 1;
            } else {
                break;
            }
        }

        // Take the middle element if there are odd numbers of data, and take
        // the avg of the middle two elements for even numbers of data
        if (row % 2 == 1) {
            median = data[row / 2];
        } else {
            median = (data[row / 2 - 1] + data[row / 2]) / 2;
        }
        return median;
    }

    /* Add column c1 and column c2 and return the result */
    public CSVData add(int c1, int c2) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (int i = 0; i < row; i++) {
            List<Double> list = new ArrayList<Double>();
            list.add(rawData.get(i).get(c1) + rawData.get(i).get(c2));
            data.add(list);
        }
        return new CSVData(data);
    }

    /* Subtract column c1 from column c2 and return the result */
    public CSVData sub(int c1, int c2) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (int i = 0; i < row; i++) {
            List<Double> list = new ArrayList<Double>();
            list.add(rawData.get(i).get(c1) - rawData.get(i).get(c2));
            data.add(list);
        }
        return new CSVData(data);
    }

    /* Multiply column c1 and column c2 and return the result */
    public CSVData mul(int c1, int c2) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (int i = 0; i < row; i++) {
            List<Double> list = new ArrayList<Double>();
            list.add(rawData.get(i).get(c1) * rawData.get(i).get(c2));
            data.add(list);
        }
        return new CSVData(data);
    }

    /* Divide column c1 from column c2 and return the result */
    public CSVData div(int c1, int c2) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (int i = 0; i < row; i++) {
            List<Double> list = new ArrayList<Double>();
            double dividend = rawData.get(i).get(c1);
            double divisor = rawData.get(i).get(c2);

            // Special case when divisor is 0. Use positive infinity and
            // negative infinity to represent the two cases and use zero to
            // represent 0 divide by 0
            if (divisor == 0) {
                if (dividend > 0) {
                    list.add(Double.POSITIVE_INFINITY);
                } else if (dividend < 0) {
                    list.add(Double.NEGATIVE_INFINITY);
                } else {
                    list.add(0.0);
                }
            } else {
                list.add(dividend / divisor);
            }
            data.add(list);
        }
        return new CSVData(data);
    }

    /* Perform inner/outer join on the two columns */
    public static CSVData joins(CSVData data1, int col1, CSVData data2,
            int col2, boolean isOuter) {
        List<List<Double>> joinedData = new ArrayList<List<Double>>();

        // Maps col1's value from the first dataset to a list of integers
        // representing row indices
        HashMap<Double, List<Integer>> map = new HashMap<>();

        // Hash set contains the common value from col1 of the first dataset and
        // col2 of the second dataset
        HashSet<Double> set = new HashSet<>();

        // Inserts col1 into the hash map
        for (int i = 0; i < data1.row; i++) {
            double val = data1.rawData.get(i).get(col1);
            if (!map.containsKey(val)) {
                List<Integer> list = new ArrayList<Integer>();
                map.put(val, list);
            }
            map.get(val).add(i);
        }

        // Traverses col2 and look for matches from the hash map
        for (int i = 0; i < data2.row; i++) {
            double val = data2.rawData.get(i).get(col2);
            if (map.containsKey(val)) {
                // Add the common val into the hash set
                set.add(val);
                List<Integer> list = map.get(val);

                // Traverses the list of rows from dataset1 that all have the
                // same val in col1
                for (int j = 0; j < list.size(); j++) {
                    int idx = list.get(j);
                    List<Double> line = new ArrayList<Double>();

                    // Add the common value as the first element
                    line.add(val);

                    // Then add the remaining values from the first dataset
                    addAllExcept(line, data1.rawData.get(idx), col1);

                    // Finally add the remaining values from the second dataset
                    addAllExcept(line, data2.rawData.get(i), col2);

                    // Add the line to the output data
                    joinedData.add(line);
                }
            }
        }

        // Full outer join
        if (isOuter) {
            // Add the unique rows from the first dataset
            for (int i = 0; i < data1.row; i++) {
                double val = data1.rawData.get(i).get(col1);
                if (!set.contains(val)) {
                    List<Double> line = new ArrayList<Double>();

                    // Add the first dataset
                    line.addAll(data1.rawData.get(i));

                    // Then add null since val is unique in the first dataset
                    line.add(null);

                    joinedData.add(line);
                }
            }

            // Add the unique rows from the second dataset
            for (int i = 0; i < data2.row; i++) {
                double val = data2.rawData.get(i).get(col2);
                if (!set.contains(val)) {
                    List<Double> line = new ArrayList<Double>();

                    // Add null since val is unique in the second dataset
                    line.add(null);

                    // Then add the second dataset
                    line.addAll(data2.rawData.get(i));

                    joinedData.add(line);
                }
            }
        }

        CSVData joinedCSV = new CSVData(joinedData);
        return joinedCSV;
    }

    /* Helper function to partition the array */
    private int partition(double[] data, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        boolean finish = false;
        while (!finish) {
            // Find element from the left to swap
            while (i < hi && data[++i] < data[lo])
                ;
            // Find element from the right to swap
            while (j > lo && data[--j] > data[lo])
                ;
            // Skip if two pointers cross with each other
            if (i >= j) {
                finish = true;
                break;
            }
            swap(data, i, j);
        }
        // Swap the pivot
        swap(data, lo, j);
        return j;
    }

    /* Helper function to swap two elements in the array */
    private void swap(double[] data, int i, int j) {
        double temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /* Helper function to shuffle the array */
    private void shuffle(double data[]) {
        Random random = new Random();
        for (int ind = 1; ind < data.length; ind++) {
            int r = random.nextInt(ind + 1);
            swap(data, ind, r);
        }
    }

    /* Helper function to add all elements except for the input index */
    private static void addAllExcept(List<Double> dst, List<Double> src, int col) {
        for (int k = 0; k < src.size(); k++) {
            if (k != col) {
                dst.add(src.get(k));
            }
        }
    }
}
