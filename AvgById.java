
package bibi;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
public class AvgById {
public static void main(String [] args) throws Exception
{
Configuration c=new Configuration();
String[] files=new GenericOptionsParser(c,args).getRemainingArgs();
Path input=new Path(files[0]);
Path output=new Path(files[1]);
Job j=new Job(c,"average");
j.setJarByClass(AvgById.class);

j.setMapperClass(MapForAvgById.class);
j.setReducerClass(ReduceForAvgById.class);

j.setOutputKeyClass(Text.class);
j.setOutputValueClass(FloatWritable.class);

FileInputFormat.addInputPath(j, input);
FileOutputFormat.setOutputPath(j, output);
System.exit(j.waitForCompletion(true)?0:1);
}
public static class MapForAvgById extends Mapper<LongWritable, Text, Text, FloatWritable>{
public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
{
	String line = value.toString();
	String[] words=line.split(",");

	System.out.println(words[0] + " >>> " + words[2]);
	Text outputKey_INDEX = new Text(words[0]);
	FloatWritable outputValue_GPA = new FloatWritable( Float.parseFloat(words[2]) );

	con.write(outputKey_INDEX, outputValue_GPA);
}
}

public static class ReduceForAvgById extends Reducer<Text, FloatWritable, Text, FloatWritable>
{
public void reduce(Text word, Iterable<FloatWritable> values, Context con) throws IOException, InterruptedException
{
	// ??? FloatWritable avg = new FloatWritable(0);

	float sum = 0;	
	int count = 0;	

	for(FloatWritable value : values)
   	{
		sum += Float.parseFloat(value.toString());
		count += 1;

		// ??? avg += value/values.size()
   	}

	float avg = sum / count;
	con.write(word, new FloatWritable(avg));
	
	System.out.println(word + "	" + avg);
}
}

}
