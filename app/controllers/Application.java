package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Date; 


import org.apache.http.HttpResponse;
//import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import play.mvc.Controller;
import play.mvc.With;
import play.*;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import play.mvc.*;
import play.Logger;

/*import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;*/


import org.apache.http.util.EntityUtils;

import models.*;

public class Application extends Controller {
	static Logger logInfo = new Logger();
    public static void index() {
        render();
    }

    public static void getBosonNlp(){
    	String content = null;
		content = httpClientPost("http://api.bosonnlp.com/tag/analysis","UTF-8");
		
    }
    
    public static String toUnicode(String str){
        char[]arChar=str.toCharArray();
        int iValue=0;
        String uStr="";
        for(int i=0;i<arChar.length;i++){
            iValue=(int)str.charAt(i);          
            if(iValue<=256){
              // uStr+="& "+Integer.toHexString(iValue)+";";
                uStr+="\\"+Integer.toHexString(iValue);
            }else{
              // uStr+="&#x"+Integer.toHexString(iValue)+";";
                uStr+="\\u"+Integer.toHexString(iValue);
            }
        }
        return uStr;
    }    
    
    
    private static String httpClientPost(String path, String charSet) {
    	
    	System.out.println("httpClientPost");
    	
    	
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(path);
        post.setHeader( "X-Token" , "hAkOGN1L.3035.Kuz81p97h9h-" );
        post.addHeader( "X-Token" , "hAkOGN1L.3035.Kuz81p97h9h-" );
        
        final String requestContent = "15日，备受关注的电影《黄金时代》在北京举行了电影发布会，导演许鞍华和编剧李樯及汤唯、冯绍峰等众星悉数亮相。"
        					  + "据悉，电影确定将于10月1日公映。本片讲述了“民国四大才女”之一的萧红短暂而传奇的一生，通过她与萧军、汪恩甲、"
        					  + "端木蕻良、洛宾基四人的情感纠葛，与鲁迅、丁玲等人一起再现上世纪30年代的独特风貌。电影原名《穿过爱情的漫长旅程》，"
        					  + "后更名《黄金时代》，这源自萧红写给萧军信中的一句话：“这不正是我的黄金时代吗";
        
        
        final String requestStr = toUnicode(requestContent);
        
        System.out.println(requestStr);
     //   HttpJsonUtil jsonUtil = new HttpJsonUtil();
        
        
        String responseContent = null;
        System.out.println("responseContent");
        
        System.out.println();
        
        try {
            ContentProducer cp = new ContentProducer() {
                public void writeTo(OutputStream outstream) throws IOException {
                    Writer writer = new OutputStreamWriter(outstream, "UTF-8");
                    String params = "{\""+requestStr+"\"}"; 
                    writer.write(params);
                    writer.flush();
                }
            };
            
            
            System.out.println("ContentProducer()");
            
            post.setEntity(new EntityTemplate(cp));
            HttpResponse response = client.execute(post);
            responseContent = EntityUtils.toString(response.getEntity());
            
            System.out.println("setEntity");
            
            try{
         //   	jsonUtil.parseHttpContent(responseContent);
            	System.out.println(response.getEntity());
            	System.out.println(responseContent);
            	logInfo.info("load data success");
            }catch (Exception e){
 
            	System.out.println(e);
            	logInfo.error(e.toString(),e); 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            logInfo.error(e.toString(),e); 
        } 
        
        return responseContent;
    }
    
    
    public static String getWordAnalysisData(String path, String charSet){
    	String jsonObj = "[{\"word\":[\"15日\",\"，\",\"备受\",\"关注\",\"的\",\"电影\",\"《\",\"黄金时代\",\"》\","
					   + "\"在\",\"北京\",\"举行\",\"了\",\"电影\",\"发布会\",\"，\",\"导演\",\"许\",\"鞍\","
					   + "\"华\",\"和\",\"编剧\",\"李\",\"樯\",\"及\",\"汤唯\",\"、\",\"冯绍峰\",\"等\",\"众\","
					   + "\"星\",\"悉数\",\"亮相\",\"。\",\"据悉\",\"，\",\"电影\",\"确定\",\"将\",\"于\",\"10月\","
					   + "\"1日\",\"公映\",\"。\",\"本片\",\"讲述\",\"了\",\"“\",\"民国\",\"四大\",\"才女\",\"”\","
					   + "\"之一\",\"的\",\"萧红\",\"短暂\",\"而\",\"传奇\",\"的\",\"一生\",\"，\",\"通过\",\"她\","
					   + "\"与\",\"萧军\",\"、\",\"汪恩甲\",\"、\",\"端木\",\"蕻\",\"良\",\"、\",\"洛宾基\",\"四\","
					   + "\"人\",\"的\",\"情感\",\"纠葛\",\"，\",\"与\",\"鲁迅\",\"、\",\"丁玲\",\"等\",\"人\","
					   + "\"一起\",\"再现\",\"上\",\"世纪\",\"30\",\"年代\",\"的\",\"独特\",\"风貌\",\"。\","
					   + "\"电影\",\"原名\",\"《\",\"穿过\",\"爱情\",\"的\",\"漫长\",\"旅程\",\"》\",\"，\",\"后\","
					   + "\"更名\",\"《\",\"黄金时代\",\"》\",\"，\",\"这\",\"源自\",\"萧红\",\"写\",\"给\",\"萧军信\","
					   + "\"中\",\"的\",\"一\",\"句\",\"话\",\"：\",\"“\",\"这\",\"不\",\"正\",\"是\",\"我\",\"的\","
					   + "\"黄金时代\",\"吗\",\"？\",\"”\"],\"tag\":[\"t\",\"wd\",\"v\",\"v\",\"ude1\",\"n\",\"wkz\","
					   + "\"t\",\"wky\",\"p\",\"ns\",\"v\",\"ule\",\"n\",\"n\",\"wd\",\"n\",\"nr1\",\"ng\",\"ag\",\"cc\","
					   + "\"n\",\"nr1\",\"ng\",\"cc\",\"nr\",\"wn\",\"nr\",\"udeng\",\"ng\",\"n\",\"d\",\"vi\",\"wj\","
					   + "\"v\",\"wd\",\"n\",\"v\",\"d\",\"p\",\"t\",\"t\",\"v\",\"wj\",\"r\",\"v\",\"ule\",\"wyz\",\"n\","
					   + "\"n\",\"n\",\"wyy\",\"rz\",\"ude1\",\"nr\",\"a\",\"cc\",\"n\",\"ude1\",\"n\",\"wd\",\"p\","
					   + "\"rr\",\"p\",\"nr\",\"wn\",\"nr\",\"wn\",\"nr1\",\"w\",\"ng\",\"wn\",\"nrf\",\"m\",\"n\","
					   + "\"ude1\",\"n\",\"n\",\"wd\",\"p\",\"nr\",\"wn\",\"nr\",\"udeng\",\"n\",\"s\",\"v\",\"vf\","
					   + "\"n\",\"m\",\"n\",\"ude1\",\"a\",\"n\",\"wj\",\"n\",\"n\",\"wkz\",\"v\",\"n\",\"ude1\","
					   + "\"a\",\"n\",\"wky\",\"wd\",\"f\",\"vi\",\"wkz\",\"t\",\"wky\",\"wd\",\"rzv\",\"v\",\"nr\","
					   + "\"v\",\"p\",\"nr\",\"f\",\"ude1\",\"m\",\"q\",\"n\",\"wp\",\"wyz\",\"rzv\",\"d\",\"d\","
					   + "\"vshi\",\"rr\",\"ude1\",\"t\",\"y\",\"ww\",\"wyy\"]}]";
    	
    	renderJSON(jsonObj);
    	return "OK";
    }
   
    
/*    private static String httpClientPost(String path, String charSet) throws UnirestException {
    	
    	final String TEXTATTR_URL = path;
    	final String API_TOKEN = "hAkOGN1L.3035.Kuz81p97h9h-";
    	
        final String requestContent = "[\"15日，备受关注的电影《黄金时代》在北京举行了电影发布会，导演许鞍华和编剧李樯及汤唯、冯绍峰等众星悉数亮相。"
				  + "据悉，电影确定将于10月1日公映。本片讲述了“民国四大才女”之一的萧红短暂而传奇的一生，通过她与萧军、汪恩甲、"
				  + "端木蕻良、洛宾基四人的情感纠葛，与鲁迅、丁玲等人一起再现上世纪30年代的独特风貌。电影原名《穿过爱情的漫长旅程》，"
				  + "后更名《黄金时代》，这源自萧红写给萧军信中的一句话：“这不正是我的黄金时代吗\"]";
        
        System.out.println(TEXTATTR_URL);
        System.out.println(API_TOKEN);
        System.out.println(requestContent);
        
		// 文本分析API调用
        HttpResponse<JsonNode> clusterPushResponse = Unirest.post(TEXTATTR_URL)
                .header("Accept", "application/json")
                .header("Content-Type","application/json")
                .header("X-Token", API_TOKEN)
                .body(requestContent).asJson();
        
		System.out.println(clusterPushResponse.getCode());
		
        return "OK";
    }*/
    
    
}