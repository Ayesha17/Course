package com.example.ayeshashahid.course;

/**
 * Created by ayesha.shahid on 7/27/2017.
 */
public class Book {

    private String name,course_category,tutionfee,categoryId,subCategoryid,subCourse_category;
    private String imageUrl;
    private String authorName;
    String code = null;
    String levelName = null;
    boolean selected = false;


    public Book(String catid)
    {
        super();
        this.categoryId = catid;

    }

    public Book() {
        super();
    }

    public Book(String levelID, String levelName, boolean b) {
        super();
        this.code = levelID;
        this.levelName = levelName;
        this.selected = b;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCourse_category() {
        return course_category;
    }
    public void setCourse_category(String course_category) {
        this.course_category = course_category;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryid(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getTutionfee() {
        return tutionfee;
    }


    public void setTutionfee(String tutionfee) {
        this.tutionfee = tutionfee;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getLevelName() {
        return levelName;
    }
    public String getLevelId() {
        return code;
    }
    public void setLevelName(String levelname) {
        this.levelName = levelname;
    }

public void setSubCategoryid(String subCategoryid) {
        this.subCategoryid = subCategoryid;
    }
    public String getSubCategoryid(){
        return subCategoryid;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public void setSubCourse_category(String subCourse_category) {
        this.subCourse_category = subCourse_category;
    }
    public String getSubCourse_category() {
        return subCourse_category;
    }

}