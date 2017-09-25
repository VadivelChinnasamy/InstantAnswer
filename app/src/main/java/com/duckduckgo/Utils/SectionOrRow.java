package com.duckduckgo.Utils;

public class SectionOrRow {

    private String result,icon,firstURL;
    private String section,text,ImageUrl;
    private boolean isRow;
    private int id;
    private String image;

    public static SectionOrRow createRow(String result) {
        SectionOrRow ret = new SectionOrRow();
        ret.result = result;
        ret.isRow = true;
       /* ret.icon=icon;

       // ret.id = id;
        ret.firstURL=firstURL;
        ret.text=text;*/
        return ret;
    }

    public static SectionOrRow createSection(String result, String icon, String firstURL, String ImageUrl) {

        SectionOrRow ret = new SectionOrRow();
        ret.result = result;
        ret.icon=icon;
        // ret.id = id;
        ret.firstURL=firstURL;
        ret.ImageUrl=ImageUrl;
        ret.isRow = false;
        return ret;

    }


    public String getResult() {
        return result;
    }

    public String getImg() {
        return icon;
    }
    public  String getImages(){return ImageUrl;}

    public String getSection() {
        return text;
    }

    public boolean isRow() {
        return isRow;
    }

 /*   public int getId() {
        return id;
    }*/


}