package com.eulerity.hackathon.imagefinder;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.mockito.Mockito;

import com.eulerity.hackathon.imagefinder.ImageFinder;
import com.google.gson.Gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ScraperTest {

    @Test
    public void testInvalidCrawl(){
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

        String url = "pizza";
        int depth = 5;
        ConcurrentHashMap<String, List<Image>> imageHandler = new ConcurrentHashMap<>();
        Scraper scraper = new Scraper(url, depth, threadPoolExecutor, imageHandler);
        Exception e =  Assert.assertThrows(Exception.class, () -> scraper.crawl());
        System.out.println(e.getMessage());
        assertEquals("Malformed URL: pizza", e.getMessage());
    }
    /*@Test
    public void ValidCrawl(){
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        String url = "https://www.pizzahut.com";
        int depth = 5;
        ConcurrentHashMap<String, List<Image>> imageHandler = mock(ConcurrentHashMap.class);
        when(imageHandler.get(url)).thenReturn(new ArrayList<>());
        Scraper scraper = new Scraper(url, depth, threadPoolExecutor, imageHandler);
        Exception exception =  Assert.assertThrows(Exception.class, () -> scraper.crawl());
        verify(imageHandler, times(1)).get(url);
        verify(imageHandler, times(1)).replace(url, new ArrayList<>());
    }*/
}
