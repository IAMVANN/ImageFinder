package com.eulerity.hackathon.imagefinder;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

public class ImageFinderTest {

	public HttpServletRequest request;
	public HttpServletResponse response;
	public StringWriter sw;
	public HttpSession session;
    //having trouble mocking the request and response objects, will come back to this later if given the time - other wise will do system testing
	/*@Before
	public void setUp() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
    sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
		Mockito.when(response.getWriter()).thenReturn(pw);
		Mockito.when(request.getRequestURI()).thenReturn("/foo/foo/foo");
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/foo/foo/foo"));
		session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
	}
	*
  /*@Test
  public void test() throws IOException, ServletException {
		Mockito.when(request.getServletPath()).thenReturn("/main");
		new ImageFinder().doPost(request, response);
		Assert.assertEquals(new Gson().toJson(ImageFinder.testImages), sw.toString());
  }*/
	/*@Test
	public void testInvalidUrl(){
		Mockito.when(request.getServletPath()).thenReturn("/main");
		Mockito.when(request.getParameter("url")).thenReturn("Pleasegivingmeinternshippls");
		try {
			new ImageFinder().doPost(request, response);
		} catch (IOException | ServletException e) {
			Assert.assertEquals("Unable to access website or faulty website", e.getMessage());
		}
		Assert.
	}*/
  @Test
  public void testGetImages() {
	  List<Image> testImages = new ArrayList<>();
	  Image testImage = new Image("http://example.com", "http://example.com/image.jpg", "Example Image", "image", "image1");
	  testImages.add(testImage);
	  ImageFinder imageFinder = new ImageFinder();
	  imageFinder.pulledImages = new ConcurrentHashMap<>();
	  imageFinder.pulledImages.put("http://example.com", testImages);
	  List<String> imageUrls = imageFinder.getImages();
	  assertEquals(1, imageUrls.size());
  }
}



