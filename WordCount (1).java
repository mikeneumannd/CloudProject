
//This is a test



import java.io.IOException;
import java.util.StringTokenizer;
import java.util.HashMap;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class WordCount {

	public static void main(String[] args)
        throws IOException, ClassNotFoundException, InterruptedException {
        if (args.length != 2) {
            System.err.println("Usage: Word Count <input path> <output path>");
            System.exit(-1);
        }

        Job job = new Job();
        job.setJarByClass(WordCount.class);
        job.setJobName("Word Count");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.waitForCompletion(true);
    }
}

class WordCountMapper extends Mapper<LongWritable, Text, Text, Text>
{
    private Text word = new Text();

    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
    {
        String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);
	String alphaNum = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

        while (tokenizer.hasMoreTokens())
        {
	    String nextWord = tokenizer.nextToken();
	    while (true) {
				if (nextWord.length() == 1) {
					break;
				} else if (alphaNum.contains(String.valueOf(nextWord.charAt(0)))) {
					break;
				} else {
					nextWord = nextWord.substring(1);
				}
			}
			
			while (true) {
				if (nextWord.length() == 1) {
					break;
				} else if (alphaNum.contains(String.valueOf(nextWord.charAt(nextWord.length()-1)))) {
					break;
				} else {
					nextWord = nextWord.substring(0, nextWord.length()-1);
				}
			}
            word.set(nextWord);
            context.write(word, new Text(fileName));
        }
    }
}

class WordCountReducer extends Reducer<Text, Text, Text, Text>
{
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException
    {
        HashMap output = new HashMap();

        for (Text text: values) {
            String word = text.toString();
            if (output == null || output.get(word) == null) {
                output.put(word, 1);
            } else {
                output.put(word, ((int) output.get(word)) + 1);
            }
        }

        context.write(key, new Text(output.toString()));
    }
}
