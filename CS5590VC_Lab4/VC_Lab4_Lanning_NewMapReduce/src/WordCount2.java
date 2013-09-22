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

public class WordCount2 
{
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text>
    {
        private final static LongWritable one = new LongWritable(1);
        private Text word = new Text();

        /**
         * @see org.apache.hadoop.mapred.Mapper#map(java.lang.Object, java.lang.Object, org.apache.hadoop.mapred.OutputCollector, org.apache.hadoop.mapred.Reporter)
         */
        @Override
        public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> out, Reporter reporter) throws IOException
        {
            String line = value.toString();
            StringTokenizer stringTokenizer = new StringTokenizer(line);
            long num = 1000; 
            String word = "none";

            if(stringTokenizer.hasMoreTokens())
            {
                String s1 = stringTokenizer.nextToken();
                word = s1.trim();
            }

            if(stringTokenizer.hasMoreElements())
            {
                String s2 = stringTokenizer.nextToken();
                num = Long.parseLong(s2.trim());
            }

            out.collect(new LongWritable(num), new Text(word));
        }
        
        public static class Reduce extends MapReduceBase implements Reducer<Text, LongWritable, Text, LongWritable>
        {
            /**
             * @see org.apache.hadoop.mapred.Reducer#reduce(java.lang.Object, java.util.Iterator, org.apache.hadoop.mapred.OutputCollector, org.apache.hadoop.mapred.Reporter)
             */
            public void reduce(Text key, Iterator<LongWritable> values, OutputCollector<Text, LongWritable> out, Reporter reporter) throws IOException
            {
                while((values.hasNext()))
                {
                    out.collect(key, values.next());
                }
            }
        }
    }
    
	public static void main(String[] args) 
	{
    	JobClient client = new JobClient();
    	JobConf conf = new JobConf(WordCount2.class);
    	
    	FileInputFormat.addInputPath(conf, new Path("/home/idcuser/"));/// The dir here is the Hadoop directory
    	FileOutputFormat.setOutputPath(conf, new Path("output_lab"));
    	conf.setOutputKeyClass(Text.class);
    	conf.setOutputValueClass(LongWritable.class);
    	
    	conf.setMapperClass(Map.class);
    	conf.setCombinerClass(WordCount2.Map.Reduce.class); 
    	conf.setReducerClass(WordCount2.Map.Reduce.class); 
    	
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

