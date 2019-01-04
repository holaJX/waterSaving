package com.dyzhsw.efficient.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping(value = "/newscontroller")
@Api(value = "咨询列表获取")
public class NewsController{
	
	
	
	@RequestMapping(value = "/getInforList",method=RequestMethod.POST)
	@ApiOperation(value="获取资讯列表信息",notes="水利要闻和行业资讯")
	@ApiImplicitParams({@ApiImplicitParam(name = "value", value = "2或者13",paramType = "query" ,required = false, dataType = "String")})
	public BaseResponse getNews(String value) throws IOException{
		
		List<News> news = new ArrayList<News>();
		int specialClass=2;
		if("行业资讯".equals(value)){
			specialClass=13;
		}
		
		Document document = getDocument("http://www.jsgg.com.cn/Index/SpecialNews.asp?SpecialClass="+specialClass);
		Elements newsList = document.select("tbody > tr > td > a");
		//System.out.println(newsList);
		for (Element element : newsList) {
			String title=element.select("a").attr("title");
			//System.out.println(title);
			if(!"".equals(title)){
				News n = new News();
				//String date[] = element.select("a").attr("href").split("/");
				//n.setId(date[2].split("\\.")[0]);
				String hrefd=element.select("a").attr("href");
				int index=hrefd.indexOf(".");
				String hrefn= hrefd.substring(index+2);;
				n.setTitle(title);
				//n.setDate(element.select("em").text());
				n.setLink("http://www.jsgg.com.cn" + hrefn);
				news.add(n);
			}			
		}
		
		return BaseResponseUtil.success(200, "查询成功", news);
	}
	// get获取新闻网站dom对象
		public Document getDocument(String url) throws IOException {
			return Jsoup.connect(url).get();
		}
}
