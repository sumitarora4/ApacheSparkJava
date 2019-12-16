package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<Integer> inputData = new ArrayList<>();
		inputData.add(35);
		inputData.add(12);
		inputData.add(90);
		inputData.add(20);
		
		Logger.getLogger("org.apache").setLevel(Level.WARN); // showing only warn level logs 
		
		SparkConf conf = new SparkConf()
				.setAppName("startingSpark")
				.setMaster("local[*]"); // run on available cores
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		JavaRDD<Integer> myRdd = sc.parallelize(inputData);
		
		/*
		 * Reduce Method
		 */
		
//		Integer result = myRdd.reduce( (Integer value1, Integer value2) -> value1 + value2 );

//				 defining Integer as a datatype in reduce function is optional
		Integer result = myRdd.reduce( ( value1,  value2) -> value1 + value2 );
		
		System.out.println(result);
		
		
		/*
		 * Mapping Element of an RDD
		 */
		
		JavaRDD<Double> sqrtRdd = myRdd.map(value -> Math.sqrt(value));
			
//		sqrtRdd.foreach(value -> System.out.println(value));
		
		//OR
		
		sqrtRdd.collect().forEach( System.out::println );
		
		
		
		/*
		 * Count the element of RDD
		 */
		
		System.out.println(sqrtRdd.count());
		
		// without using count function i.e. using map and reduce functions
		
		JavaRDD<Long> singleRdd = sqrtRdd.map(value -> 1L);
		
		Long elementCount = singleRdd.reduce((value1,value2) -> value1 + value2);
		
		System.out.println("elementCount="+elementCount);
	
		
		
		sc.close();
		
		
		
		
		
				
	 
		  
	}

}
