package com.eulerity.hackathon.imagefinder;

public class Image { //represents an image and all of their possible attributes, and a few additional metrics like if it is a logo or favicon
    private String sourceURL; //The URL of the website where the image was found
    private String url;
    private String altText;
    private String className;
    private String logo;
    private String id;

    public Image(String sourceURL, String url, String altText, String className, String id){
        this.sourceURL = sourceURL;
        this.url = url;
        this.altText = altText;
        this.className = className;
        this.id = id;
        this.logo = "Image";
    }


    public String getUrl(){
        return url;
    }
    public String getAltText(){
        return altText;
    }
    public String getClassName(){
        return className;
    }
    public String getId(){
        return id;
    }
    public String isLogo(){
        //determines if an image is a logo, favicon, or regular image by checking to see if they have logo or favicon in any of their attributes
        if(this.url.toLowerCase().contains("logo") || this.altText.toLowerCase().contains("logo") || this.className.toLowerCase().contains("logo") || this.id.toLowerCase().contains("logo")){
            logo = "Logo";
        }
        if(this.url.toLowerCase().contains("favicon") || this.altText.toLowerCase().contains("favicon") || this.className.toLowerCase().contains("favicon") || this.id.toLowerCase().contains("favicon")){
            logo = "Favicon";
        }
        return logo;
    }
    public String getLogo(){
        return logo;
    }

    public String getSourceURL() {
        return sourceURL;
    }
}
