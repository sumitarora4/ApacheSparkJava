package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;

import scala.Tuple2;

public class Joins {
	
	public static void main(String[] args){
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf = new SparkConf().setAppName("Joins").setMaster("local[*]");
		JavaSparkContext sc =new JavaSparkContext(conf);
		
		List<Tuple2<Integer, Integer>> visitsRaw = new ArrayList<>();
		visitsRaw.add(new Tuple2<>(4, 18)) ;
		visitsRaw.add(new Tuple2<>(6, 4)) ;
		visitsRaw.add(new Tuple2<>(10, 9)) ;
		
		List<Tuple2<Integer, String>> usersRaw = new ArrayList<>();
		usersRaw.add(new Tuple2<>(1, "John")) ;
		usersRaw.add(new Tuple2<>(2, "Bob")) ;
		usersRaw.add(new Tuple2<>(3, "Alan")) ;
		usersRaw.add(new Tuple2<>(4, "Doris")) ;
		usersRaw.add(new Tuple2<>(5, "Marq")) ;
		usersRaw.add(new Tuple2<>(6, "Requel")) ;
		
		//conver list to paried RDD
		JavaPairRDD<Integer, Integer> visits = sc.parallelizePairs(visitsRaw);
		JavaPairRDD<Integer, String> users = sc.parallelizePairs(usersRaw);
		
		//1- inner join
		JavaPairRDD<Integer, Tuple2<Integer, String>> joinedRdd = visits.join(users);
		
		//2- left outer join
		JavaPairRDD<Integer, Tuple2<Integer, Optional<String>>> leftOuterJoinedRdd = visits.leftOuterJoin(users);
//		leftOuterJoinedRdd.foreach(f -> System.out.println(f._2._2.orElse("Blank").toUpperCase()));
		
		//3- full joins 	
		JavaPairRDD<Integer, Tuple2<Optional<Integer>, Optional<String>>> fullOuterJoinRdd = visits.fullOuterJoin(users);
//		fullOuterJoinRdd.foreach( f-> System.out.println(f));
		
		//4- cartesians	
		JavaPairRDD<Tuple2<Integer, Integer>, Tuple2<Integer, String>> cartesiansRDD = visits.cartesian(users);
		cartesiansRDD.foreach( f-> System.out.println(f));
		
	}

}
