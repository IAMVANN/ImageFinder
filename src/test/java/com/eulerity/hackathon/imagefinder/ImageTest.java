package com.eulerity.hackathon.imagefinder;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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

public class ImageTest {

    @Test
    public void testGetUrl() {
        String sourceURL = "http://example.com";
        String url = "http://example.com/image.jpg";
        String altText = "Example Image";
        String className = "image";
        String id = "image1";

        Image image = new Image(sourceURL, url, altText, className, id);

        assertEquals(url, image.getUrl());
    }

    @Test
    public void testIsLogo() {
        String sourceURL = "http://example.com";
        String url = "http://example.com/logo.png";
        String altText = "Company logo";
        String className = "logo";
        String id = "logo1";

        Image image = new Image(sourceURL, url, altText, className, id);

        assertEquals("Logo", image.isLogo());
    }

    @Test
    public void testGetLogo() {
        String sourceURL = "http://example.com";
        String url = "http://example.com/favicon.ico";
        String altText = "Company Favicon";
        String className = "favicon";
        String id = "favicon1";

        Image image = new Image(sourceURL, url, altText, className, id);
        System.out.println(url.contains("favicon"));
        assertEquals("Favicon", image.isLogo());
    }
    @Test
    public void testRegularImage() {
        String sourceURL = "http://example.com";
        String url = "http://example.com/image.ico";
        String altText = "Company image";
        String className = "image";
        String id = "image";

        Image image = new Image(sourceURL, url, altText, className, id);

        assertEquals("Image", image.isLogo());
    }

    @Test
    public void testGetSourceURL() {
        String sourceURL = "http://example.com";
        String url = "http://example.com/image.jpg";
        String altText = "Example Image";
        String className = "image";
        String id = "image1";
        Image image = new Image(sourceURL, url, altText, className, id);

        assertEquals(sourceURL, image.getSourceURL());
    }
}

