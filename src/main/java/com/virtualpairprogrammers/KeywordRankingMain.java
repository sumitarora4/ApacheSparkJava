package com.virtualpairprogrammers;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class KeywordRankingMain {
	
	public static void main(String[] args){
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf = new SparkConf().setAppName("keywordRanking").setMaster("local[*]");
		JavaSparkContext sc =new JavaSparkContext(conf);
		
		JavaRDD<String> intialRdd = sc.textFile("src/main/resources/subtitles/input.txt");
		
		// replace special characters with nothing
		// //s is used for space
		// ^ is used for not in regular expression
		JavaRDD<String> lettersOnlyRdd = intialRdd.map(s -> s.replaceAll("[^a-zA-Z\\s]", "").toLowerCase());
		
		// remove blank lines
		JavaRDD<String> removedBlankLinesRdd = lettersOnlyRdd.filter(f -> f.trim().length() >0);
		
		// need just words not lines
		JavaRDD<String> justWordsRDD = removedBlankLinesRdd.flatMap(f -> Arrays.asList(f.split(" ")).iterator());
		
		// remove blank lines
		JavaRDD<String> removedBlankWordsRdd = justWordsRDD.filter(f -> f.trim().length() >0);

		// remove boring words from input by checking boringWords file stored through Util.java file
		JavaRDD<String> justInterestingWords = removedBlankWordsRdd.filter(w -> Util.isNotBoring(w));
		
		// converting into key value format
		JavaPairRDD<String, Long> pairedRdd = justInterestingWords.mapToPair(f -> new Tuple2<String, Long>(f, 1L));
		
		// adding all key values
		JavaPairRDD<String, Long> totals = pairedRdd.reduceByKey((value1, value2) -> value1 + value2);
		
		// switching numerical values at key level
		JavaPairRDD<Long, String> switched = totals.mapToPair(tuple -> new Tuple2<Long, String>(tuple._2, tuple._1));
		
		// sorting keys in descending order
		JavaPairRDD<Long, String> sorted = switched.sortByKey(false);
		
		sorted.take(10).forEach(System.out:: println);
		
		sc.close();
	}

}
