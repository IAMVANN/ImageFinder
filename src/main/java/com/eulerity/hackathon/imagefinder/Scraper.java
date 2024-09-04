package com.eulerity.hackathon.imagefinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class Scraper implements Runnable{
    private String url;
    private ThreadPoolExecutor threadPoolExecutor;
    private ConcurrentHashMap<String, List<Image>> imageHandler; //Key holds site url, value holds images;
    private int depth;
    private Document doc;


    public Scraper(String url, int depth, ThreadPoolExecutor threadPoolExecutor, ConcurrentHashMap<String, List<Image>> imageHandler ){
        //We are passing in access to the threadPOolExeecutor and the imageHandler so we can create new tasks for the threadPoolExecutor's threads
        //so we can add images to the imageHandler and
        this.url = url;
        this.depth = depth;
        this.threadPoolExecutor = threadPoolExecutor;
        this.imageHandler = imageHandler;
        List<Image> pulledImages = Collections.synchronizedList(new ArrayList<>()); //Thread safe list
        this.imageHandler.put(url, pulledImages);
    }

    public void run() {
        try {
            getImages();
            crawl();

        } catch (Exception e) {
            //System.out.println("Scrapping Failed: " + e.getMessage());
            // its honestly okay for scraping to fail on all sites besides the initial site; Not exactally an error;
        }
        shutdown();
    }
    public void shutdown(){
        //System.out.println(threadPoolExecutor.getTaskCount() + " vs " + threadPoolExecutor.getCompletedTaskCount());
        if(threadPoolExecutor.getTaskCount() -1 <= threadPoolExecutor.getCompletedTaskCount()){
            System.out.println("SHUTTING DOWN; ");
            threadPoolExecutor.shutdownNow();
        }
    }

    public void crawl() throws Exception {
        //Crawls the website to a depth of 5 (goes maximum 5 subpages deep) and adds the images to the imageHandler

        if (depth <= 5){ //Just so we don't just continously crawl all throughout the website; We stop once we reach a depth of 5;
            Document doc = Jsoup.connect(url).get();
            if (doc == null){
                throw new Exception("Unable to access website or faulty website");
            }
            Elements links = doc.getElementsByTag("a");
            //finds subPages of the page, and traverses them
            for (Element link : links){
                String pageURL = link.absUrl("href");
                //We only want to crawl on websites in the same domain right;
                if(pageURL.startsWith(url) && imageHandler.containsKey(pageURL) == false){

                    //ensures that the new crawler are in the same domain and we aren't visiting a site that we already crawled
                    Scraper crawler = new Scraper(pageURL, depth + 1, threadPoolExecutor, imageHandler);
                    threadPoolExecutor.execute(crawler); //multithreading crawling down the subpages;
                }

            }
        }
    }

    public void getImages() throws Exception {
        //Grabs all the images from the website
        Document doc = Jsoup.connect(url).get();
        if (doc == null){
            throw new Exception("Unable to access website or faulty website");
        }
        Elements images = doc.getElementsByTag("img");
        if(imageHandler.get(url).size() > 0){
            throw new Exception("Trying to Scrape Data off of an site that is already scraped at getImages" + url);
        }

        List<Image> pulledImages = imageHandler.get(url);

        for (Element siteImages : images) {
            //Grabbing all meaningful attributues from each tag and checking to see if its a logo or favicon
            String linkURL = siteImages.absUrl("src");
            String altText = siteImages.attr("alt");
            String className = siteImages.attr("class");
            String id = siteImages.attr("id");
            Image image = new Image(url, linkURL, altText, className, id);
            image.isLogo();
            pulledImages.add(image);
        }
        imageHandler.replace(url, pulledImages);
        //System.out.println("GOT IMAGES");
    }
}
