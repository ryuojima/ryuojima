/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.ryuojima.util.scraping;

import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.DefaultCssErrorHandler;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.util.Map;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;

/**
 *
 * @author hm-p0229
 */
public class CustomWebDriver extends HtmlUnitDriver{
	
	private final Map<String,String>	_requestHeader;
	
	public CustomWebDriver(boolean enableJavascript, Map<String,String> requestHeader){
		super(enableJavascript);
		this._requestHeader = requestHeader;
	}
	public WebResponse getWebResponse(){
		Page page=lastPage();
		if (page == null) {
			return null;
		}
		return page.getWebResponse();
	}
	@Override
	public String getPageSource(){
		Page page=lastPage();
		if (page == null) {
			return null;
		}

		WebResponse response=page.getWebResponse();
		return response.getContentAsString();
	}
	@Override
	protected WebClient modifyWebClient(WebClient client) {
		client.setIncorrectnessListener(new IncorrectnessListener(){
			@Override
			public void notify(String arg0, Object arg1) {}
		});
		client.setCssErrorHandler(new DefaultCssErrorHandler(){
//					@Override
//					public void error( CSSParseException e ) throws CSSException {				super.error( e ) ;				}
//					@Override
//					public void fatalError( CSSParseException e ) throws CSSException {		super.fatalError( e ) ;		}
			@Override
			public void warning( CSSParseException e ) throws CSSException {}
		});
		client.setConfirmHandler(new ConfirmHandler(){
					 @Override
					 public boolean handleConfirm(Page page, String message) {
							 return true;
					 }
		});
		client.addRequestHeader("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
		client.addRequestHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
		client.addRequestHeader("accept-encoding", "gzip,deflate,sdch");
		if(_requestHeader != null){
			for(Map.Entry<String,String> entry : _requestHeader.entrySet()){
				client.addRequestHeader(entry.getKey(), entry.getValue());
			}
		}
		
//		client.addWebWindowListener(new CustomWebDriverWindowListener());
		
		return client;
	}
	@Override
	public WebClient getWebClient(){
		return super.getWebClient();
	}
//	private class CustomWebDriverWindowListener implements WebWindowListener{
//
//		@Override
//		public void webWindowOpened(WebWindowEvent event) {}
//
//		@Override
//		public void webWindowContentChanged(WebWindowEvent event) {
//			WebResponse response = event.getWebWindow().getEnclosedPage().getWebResponse();
//			System.out.println(response.getLoadTime());
//			System.out.println(response.getStatusCode());
//			System.out.println(response.getContentType());
//
//			List<NameValuePair> headers = response.getResponseHeaders();
//			for(NameValuePair header: headers){
//					System.out.println(header.getName() + " : " + header.getValue());
//			}
//System.out.println(response.getContentAsString());
//			// Change or add conditions for content-types that you would to like 
//			// receive like a file.
//			if(response.getContentType().equals("text/plain")){
//					//getFileResponse(response, "target/testDownload.war");
//				System.out.println();
//			}
//		}
//
//		@Override
//		public void webWindowClosed(WebWindowEvent event) {}
//		
//	}
}
