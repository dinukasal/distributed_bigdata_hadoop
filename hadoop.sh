echo "RUNNING HADOOP.SH"
export HADOOP_HOME=/usr/local/hadoop

echo "cleanup previous input_dir..."
$HADOOP_HOME/bin/hadoop fs -rm -r input_dir 
printf "\n"

echo "make input_dir..."
$HADOOP_HOME/bin/hadoop fs -mkdir input_dir 
printf "\n"

echo "add csv file to input_dir..."
$HADOOP_HOME/bin/hadoop fs -put /home/bibi/Desktop/HADOOP/distributed.csv input_dir 
printf "\n"

echo "check files in input_dir..."
$HADOOP_HOME/bin/hadoop fs -ls input_dir/
printf "\n\n"

echo "remove existing compiled folder..."
rm -rf compiled/ && mkdir compiled

echo "remove existing output folder..."
rm -rf output_dir

echo "compile jar file"
javac -classpath hadoop-core-1.2.1.jar:commons-cli-1.2.jar -d compiled AvgById.java
jar -cvf avg.jar -C compiled/ .
printf "\n\n\n"

$HADOOP_HOME/bin/hadoop jar avg.jar bibi.AvgById input_dir/distributed.csv output_dir

printf "\n"
$HADOOP_HOME/bin/hadoop fs -ls output_dir
printf "\n"
$HADOOP_HOME/bin/hadoop fs -cat output_dir/part-r-00000
