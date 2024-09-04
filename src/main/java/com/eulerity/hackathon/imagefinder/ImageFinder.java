package com.eulerity.hackathon.imagefinder;

import java.awt.*;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.List;
import java.util.concurrent.*;

@WebServlet(
    name = "ImageFinder",
    urlPatterns = {"/main"}
)
public class ImageFinder extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected static final Gson GSON = new GsonBuilder().create();

	private ThreadPoolExecutor threadPoolExecutor;
	protected ConcurrentHashMap<String, List<Image>> pulledImages;

	//This is just a test array
	public static final String[] testImages = {
			"https://images.pexels.com/photos/545063/pexels-photo-545063.jpeg?auto=compress&format=tiny",
			"https://images.pexels.com/photos/464664/pexels-photo-464664.jpeg?auto=compress&format=tiny",
			"https://images.pexels.com/photos/406014/pexels-photo-406014.jpeg?auto=compress&format=tiny",
			"https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&format=tiny"
  	};

	private void setup(){
		//Setting up the thread pool and the hashmap
		//We will use a thread pool to scrape the images concurrently;
		//We will use a hashmap to store the images, while making sure we aren't visiting the same page twice;
		pulledImages = new ConcurrentHashMap<String, List<Image>>(); //Key holds site url, value holds images;
		int cores = Runtime.getRuntime().availableProcessors();
		int numberOfThreads = Math.max(cores - 1, 1);
		threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThreads);
	}

	private void ScrapImages(String homePageURL) {
		//Scraping the images
		try{
			Document doc = Jsoup.connect(homePageURL).get();
			if (doc == null){
				//Invalid initial URL
				//Adding an invalid image so our frontend can easily detect that the URL is invalid;
				Image invalidImage = new Image(homePageURL, "Invalid URL", "Invalid URL", "Invalid URL", "Invalid URL");
				List<Image> invalidImages = Collections.synchronizedList(new ArrayList<>());
				invalidImages.add(invalidImage);
				pulledImages.put(homePageURL, invalidImages);
				throw new Exception("Unable to access website or faulty website");
			} else {
				//Start Crawler, and then waits until all possible pages are scarped
				Scraper crawler = new Scraper(homePageURL, 0, threadPoolExecutor, pulledImages);
				threadPoolExecutor.execute(crawler);
				try{
					//runs until all tasks are completed
					while (!threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
						System.out.println("Still waiting for the completion of all tasks...");
					}
				} catch (Exception e){
					System.out.println("A problem occured while waiting!" + e.getMessage());
				}
			}
		} catch (Exception e ){
			System.out.println(e.getMessage());
		}





	}
	protected List<String> getImages(){
		//At the  time getImages is called, all the images should be scarped and saved to found images;
		//We will now convert the images to a json string and return it;

		List<String> imageUrls = new ArrayList<>();
		HashSet<String> foundImages = new HashSet<>();
		for(List<Image> images: pulledImages.values()){
			for(Image image: images){
				if(foundImages.contains(image.getUrl())){//stops showing duplicate images
					continue;
				}
				foundImages.add(image.getUrl());
				imageUrls.add(GSON.toJson(image));
			}
		}
		System.out.println(imageUrls.size());

		return imageUrls;
	}

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("STARTED BACKEND");
		long startTime = System.currentTimeMillis();
		resp.setContentType("text/json");
		String path = req.getServletPath();
		String url = req.getParameter("url");
		//Scrapping

		setup(); //Setting up the thread pool and the hashmap
		ScrapImages(url); //Scraping the images
		List<String> imageUrls = getImages(); //Getting the images
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("total time ran is " + totalTime + " miliseconds"); //used for performance analysis
		resp.getWriter().print(GSON.toJson(imageUrls));
	}
}
