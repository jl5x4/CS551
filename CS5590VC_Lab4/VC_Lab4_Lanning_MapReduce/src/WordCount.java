import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class WordCount 
{
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, LongWritable>
    {
        private final static LongWritable one = new LongWritable(1);
        private Text word = new Text();

        /**
         * @see org.apache.hadoop.mapred.Mapper#map(java.lang.Object, java.lang.Object, org.apache.hadoop.mapred.OutputCollector, org.apache.hadoop.mapred.Reporter)
         */
        @Override
        public void map(LongWritable key, Text value, OutputCollector<Text, LongWritable> out, Reporter reporter) throws IOException
        {
            String line = value.toString();
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens())
            {
                word.set(st.nextToken());
                out.collect(word, one);
            }
        }
        
        public static class Reduce extends MapReduceBase implements Reducer<Text, LongWritable, Text, LongWritable>
        {
            public void reduce(Text key, Iterator<LongWritable> values, OutputCollector<Text, LongWritable> out, Reporter reporter) throws IOException
            {
                int wordSum = 0;
                while (values.hasNext())
                {
                    wordSum += values.next().get();
                    out.collect(key, new LongWritable(wordSum));
                }
            }
        }
    }
    
	public static void main(String[] args) 
	{
    	JobClient client = new JobClient();
    	JobConf conf = new JobConf(WordCount.class);
    	
    	FileInputFormat.addInputPath(conf, new Path("/home/idcuser/"));/// The dir here is the Hadoop directory
    	FileOutputFormat.setOutputPath(conf, new Path("output_lab"));
    	conf.setOutputKeyClass(Text.class);
    	conf.setOutputValueClass(LongWritable.class);
    	
    	conf.setMapperClass(Map.class);
    	conf.setCombinerClass(WordCount.Map.Reduce.class); 
    	conf.setReducerClass(WordCount.Map.Reduce.class); 
    	
    	conf.setInputFormat(TextInputFormat.class);
    	conf.setOutputFormat(TextOutputFormat.class);
    	
    	client.setConf(conf);
    	try 
    	{
    	    JobClient.runJob(conf);
    	} 
    	catch (Exception e) 
    	{
    	    e.printStackTrace();
    	}
	}
}

