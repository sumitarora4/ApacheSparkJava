package com.virtualpairprogrammers;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class ReadFromDisk {

	public static void main(String arg[]){
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf = new SparkConf().setAppName("readFromDisk").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		JavaRDD<String> inputFile = sc.textFile("src/main/resources/subtitles/input.txt");
		
		/*JavaRDD<String> words =*/ inputFile.flatMap(f -> Arrays.asList(f.split(" ")).iterator())
								.collect()
								.forEach(System.out:: println);
		
//		words.collect().forEach(System.out::println);
		
		sc.close();
		
		
		
	}
}
