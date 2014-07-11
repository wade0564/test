package wade.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wade.pojo.SUBInfo;
import wade.pojo.T;

@Controller
@RequestMapping("/test")
public class TestController {
	
	
	@RequestMapping("/download")
	public void fileDownload(HttpServletResponse response) throws IOException, InterruptedException{
		
		System.out.println("test");
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		
		response.addCookie(cookie);
		OutputStream out = null;
			out = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=\test"+ "\"");
			
			for(int i=0;i<100;i++){
				Thread.sleep(100);
				out.write(i);
			}
			
			out.flush();
			out.close();
	}	
	
	@RequestMapping(value="/get",method = RequestMethod.POST,headers = "content-type=application/json")
	public @ResponseBody
	T get(@RequestBody SUBInfo subInfo) {
		
		System.out.println(subInfo);
		
		return null;
		
	}	
}
