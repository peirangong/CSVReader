CSV Parser project in java

Main purpose:

This project is used to parse a csv data and it supports the following operations:

1. Display particular column(s) from the csv
2. Perform addition, subtraction, multiplication, and division on given two columns
3. Display statistics on a given column. Statistics includes min, max, median, and mean
4. Perform column based inner/full outer join on two datasets

Overview:

CSVParser.java contains the main function and CSVData.java is the data structure and it also supports the operations.

How to build:

Please change compile.txt into compile.bat and run compile.bat to build the project if you are using Windows. The code is compiled using Java 7. I have included the built .class files just in case the build doesn't work

How to use:

In the command line window, type "java CSVParser -h" to show the help screen for usage.

Here is the usage output:

Usage: java CSVParser [Options]
-h --help                                                                : display this help menu
-d --display <.csv file> <col number1> <col number2> ...                 : display the given column(s)
-s --stat <.csv file> <col number>                                       : display the stats (max, min, mean, median) of the given column from the dataset
-c --calculation <.csv file> <col number> <col number> <add/sub/mul/div> : performs +,-,*,/ on the two columns
-i --inner <.csv file> <col number> <.csv file> <col number>             : inner join of the two given datasets with the two columns
-o --outer <.csv file> <col number> <.csv file> <col number>             : full outer join of the two given datasets with the two columns

Some examples (assume the csv file is saved at the same folder as the .class file):

java CSVParser -d Book1.csv 0 1 2
This will display column 0, 1, and 2 of the csv file

java CSVParser -s Book2.csv 2
This will display the stats for column 2 of the csv file

java CSVParser -c Book3.csv 4 6 div
This will perform col4 / col6

java CSVParser -i Book1.csv 2 Book3.csv 4
This will perform inner join on col 2 of book1 and col 4 of book2. Please see the next section on the assumptions for the input and output format

java CSVParser -o Book1.csv 2 Book3.csv 4
This will perform full outer join on col 2 of book1 and col 4 of book2. Please see the next section on the assumptions for the input and output format

Assumptions and output format:

On this project, I made the following assumptions

1. The csv data contains doubles and it is complete without any missing entries or invalid inputs
2. For inner/outer joins, I assume the second dataset's column will contain no duplicated elements and the first dataset's column may contain duplicated elements
3. For divide by zero, I assume positive number divided by 0 will return Double.POSITIVE_INFINITY, negative number divided by 0 will return Double.NEGATIVE_INFINITY, and 0/0 will return 0
4. For the inner/outer join output, it will print out the data in this format:
Inner join:
common rows | rows from data1 except common val | rows from data2 except common val

Outer join (I implemented full outer join):
common rows | rows from data1 except common val | rows from data2 except common val
rows from data1 | null
null | rows from data2