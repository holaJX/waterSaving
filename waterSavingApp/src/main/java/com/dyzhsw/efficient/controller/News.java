package com.dyzhsw.efficient.controller;



/**
 * 新闻
 * @author lhl
 *
 */
public class News {
   private String id; 
	
   private String title;

   private String origin;
 
   private String date;
 
   private String seeNum;

   private String content;
 
   private String author;

   private String editer;

   private String link;
   
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getOrigin() {
	return origin;
}
public void setOrigin(String origin) {
	this.origin = origin;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getSeeNum() {
	return seeNum;
}
public void setSeeNum(String seeNum) {
	this.seeNum = seeNum;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public String getEditer() {
	return editer;
}
public void setEditer(String editer) {
	this.editer = editer;
}

public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
@Override
public String toString() {
	return "News [id=" + id + ", title=" + title + ", origin=" + origin + ", date=" + date + ", seeNum=" + seeNum
			+ ", content=" + content + ", author=" + author + ", editer=" + editer + ", link=" + link + "]";
}



   
}
