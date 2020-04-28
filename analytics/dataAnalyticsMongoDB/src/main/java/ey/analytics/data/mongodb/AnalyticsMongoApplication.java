
package ey.analytics.data.mongodb;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import ey.analytics.data.mongodb.entity.Laptop;
import ey.analytics.data.mongodb.entity.User;
import ey.analytics.data.mongodb.repository.LaptopMongoRepository;
import ey.analytics.data.mongodb.repository.UserMongoRepository;

@SpringBootApplication
public class AnalyticsMongoApplication implements CommandLineRunner {

	@Autowired
	private UserMongoRepository userRepository;
	
	@Autowired
	private LaptopMongoRepository laptopRepository;

	@Autowired
	private GridFsOperations gridOperations;
	
	public static void main(String[] args) {
		SpringApplication.run(AnalyticsMongoApplication.class, args);
	}

	public void run(String... args) throws Exception {
		
		/**
		 * 1. save/read documents(text data) to Mongo DB
		 */
		
		userRepository.deleteAll();

		User user1 = new User("Alice2", 23);
		User user2 = new User("Bob2", 38);

		userRepository.save(user1);
		userRepository.save(user2);
		
		Laptop laptop1 = new Laptop("HP", 2000);
		laptopRepository.save(laptop1);
		
		

		// fetch all data
		System.out.println("User: found with findAll():");
		System.out.println("-------------------------------");
		userRepository.findAll().stream().forEach(item -> System.out.println(item));
		
		System.out.println();
		
		System.out.println("Laptop: found with findAll():");
		System.out.println("-------------------------------");
		laptopRepository.findAll().stream().forEach(item -> System.out.println(item));
		
		System.out.println();


		/**
		 * 2. save/read an image file to MongoDB
		 */
/*
		// Define metaData
		DBObject metaData = new BasicDBObject();
		metaData.put("organization", "JavaSampleApproach");
		metaData.put("user", "alex");
		
		// 2.1 Save binary file to MongoDB
		InputStream iamgeStream = new FileInputStream("src/main/resources/flower.png");
		metaData.put("type", "image");

		String flowerImgFile = "jsa-flower.png";
		String outputflowerImgPath = "src/main/resources/flower_output.png";
		String imageFileId  = gridOperations.store(iamgeStream, flowerImgFile, "image/png", metaData).get().toString();
		System.out.println("ImageFileId = " + imageFileId);
		iamgeStream.close();
		
		
		// 2.2 query saved binary file
		MongoIterable<GridFSFile> gridFsFiles = gridOperations.find(new Query().addCriteria(Criteria.where("filename").is(flowerImgFile)));

		gridFsFiles.forEach((Consumer<GridFSFile>) file -> {
			String fileName = file.getFilename();
			System.out.println("Image File Name: " + fileName);

		});

		MongoClient mongo = new MongoClient("localhost", 27017);

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("test");

		GridFS gfsPhoto = new GridFS(db);
		GridFSDBFile imageForOutput = gfsPhoto.findOne(flowerImgFile);
		imageForOutput.writeTo(outputflowerImgPath);
		
		mongo.close();
*/
	}

}
