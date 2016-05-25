package info.ryuojima.util.scraping;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import info.ryuojima.util.scraping.exception.ScrapingDefineException;
import info.ryuojima.util.scraping.exception.ScrapingException;
import info.ryuojima.util.scraping.exception.ScrapingFormException;
import info.ryuojima.util.scraping.type.TypeFindBy;
import static info.ryuojima.util.scraping.type.TypeFindBy.URL;
import info.ryuojima.util.scraping.type.TypeWebElement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.SearchContext;

public class Operate {

  private boolean							_outLog	= true;
  private CustomWebDriver			_driver;
  private boolean							_enableJavascript;
  private boolean							_continue;
	private Map<String,String>	_requestHeader;

  public Operate(boolean enableJavascript){
		this(enableJavascript, null);
  }

  public Operate(boolean enableJavascript, Map<String,String> requestHeader){
		_enableJavascript = enableJavascript;
		_requestHeader = requestHeader;
		_driver = getWebDriver();
		_continue = false;
  }
	
  public CustomWebDriver getDriver() {
    return _driver;
  }

  public void setDriver(CustomWebDriver _driver) {
    this._driver = _driver;
  }

	public boolean isContinue() {
		return _continue;
	}
	public void setContinue(boolean _continue) {
		this._continue = _continue;
	}
	public void enableJavascript(boolean enableJavascript){
		_enableJavascript = enableJavascript;
		((HtmlUnitDriver)_driver).setJavascriptEnabled(enableJavascript);
	}
	public void setOutLog(boolean _outLog) {
		this._outLog = _outLog;
	}
	
	/**
	 * 現在のページ
	 * @return 
	 */
	public String getCurrentUrl(){
		return _driver.getCurrentUrl();
	}
	/**
	 * ブラウザの「進む」操作
	 */
	public void forward(){
		_driver.navigate().forward();
		if(_outLog){
			System.out.println(_driver.getCurrentUrl());
		}
	}
	/**
	 * ブラウザの「戻る」操作
	 */
	public void back(){
		_driver.navigate().back();
		if(_outLog){
			System.out.println(_driver.getCurrentUrl());
		}
	}
	/**
	 * ブラウザの「更新」操作
	 */
	public void refresh(){
		_driver.navigate().refresh();
	}
	public void quit(){
		if(_driver != null){
			_driver.quit();
		}
		_driver = null;
	}
	public Map<String,Object> exec(ScrapingDriver scraping) throws ScrapingException{
		if(_driver == null){
			_driver = getWebDriver();
		}
		return execItem(scraping, _driver, 0);
	}
	public Map<String,Object> exec(ScrapingDriver scraping, SearchContext context) throws ScrapingException{
		if(_driver == null){
			_driver = getWebDriver();
		}
		return execItem(scraping, context, 0);
	}
	private Map<String,Object> execItem(ScrapingDriver scraping, SearchContext context, int keyIndex) throws ScrapingException{
		if(_outLog){
			System.out.println("before : " + _driver.getCurrentUrl());
		}
		
		Map<String,Object> ret = new TreeMap<>();
		
		Object obj = null;
		try{
			obj = execDetail(scraping, context);
			if(_outLog){
				try{
					System.out.println("after : " + _driver.getCurrentUrl());
				}catch(Exception e){}
			}
		}catch(ScrapingException e){
			if(!_continue){
				throw e;
			}
			if(_outLog){
				System.out.println("after : " + _driver.getCurrentUrl() + " >> ex : " + e.getMessage());
			}
		}
		String key = scraping.getKey();
		if(key == null){
			key = String.valueOf(keyIndex);
			keyIndex++;
		}
		
		List<ScrapingDriver> child = scraping.getChild();
		//子要素あり
		if(child != null && child.size()>0){
			//実行結果がWebElement
			List<SearchContext> search = getSearchContext(obj);
			if(search != null){
				List<Map<String,Object>> mapList = new ArrayList<>();

				for (SearchContext search1 : search) {
					Map<String,Object> sc = new TreeMap<>();
					SearchContext elem = search1;
					sc.put(key, elem);
					Map<String,Object> childMap = new TreeMap<>();
					for (ScrapingDriver child1 : child) {
						Map<String,Object> retObj = execItem(child1, elem, keyIndex);
						if(retObj != null){
							childMap.putAll(retObj);
						}
					}
					sc.put("@child",childMap);
					mapList.add(sc);
				}
				if(obj instanceof WebElement){
					Map<String, Object> sc = new TreeMap<>();
					sc.put(key, obj);
					sc.put("@child", (mapList.size()>0) ? mapList.get(0) : null);
					ret.put(key, sc);
				}else if(obj instanceof List){
					ret.put(key, mapList);
				}else{
					ret.put(key, obj);
					ret.put("@child",(mapList.size()>0) ? mapList.get(0) : null);
				}
			}else{
				if(isEmpty(obj)){
					ret.put(key, null);
				}else{
					ret.put(key, obj);
					Map<String,Object> childMap = new TreeMap<>();
					for (ScrapingDriver child1 : child) {
						SearchContext next = context;
						if(obj instanceof SearchContext){
							next = (SearchContext)obj;
						}
						Map<String,Object> retObj = execItem(child1, next, keyIndex);
						if(retObj != null){
							childMap.putAll(retObj);
						}
					}
					ret.put("@child",childMap);
				}
			}
		//子要素なし
		}else{
			if(isEmpty(obj)){
				ret.put(key, null);
			}else{
				ret.put(key, obj);
			}
		}
		return ret;
	}
	private boolean isEmpty(Object obj){
		if(obj == null){
			return true;
		}
		if(obj instanceof List){
			return ((List)obj).size()<=0;
		}
		return false;
	}
	private List<SearchContext> getSearchContext(Object obj){
		List<SearchContext> ret = null;
		if(!isEmpty(obj)){
			if(obj instanceof List){
				if(((List)obj).get(0) instanceof SearchContext){
					ret = (List<SearchContext>)obj;
				}
			}else{
				if(obj instanceof SearchContext){
					ret = new ArrayList<>();
					ret.add((SearchContext)obj);
				}
			}
		}
		return ret;
	}
	private Object execDetail(ScrapingDriver scraping, SearchContext context) throws ScrapingException{
		if(_driver == null){
			_driver = getWebDriver();
		}
		TypeFindBy findBy = scraping.getFindBy();
		if(findBy == null){
			throw new ScrapingDefineException(ScrapingDefineException.TYPE.NONE_TYPE_FIND_BY, null);
		}
		switch(findBy){
			case URL:
				By by = null;
				TypeFindBy waitBy = scraping.getWaitBy();
				if(waitBy != null){
					by = waitBy.getBy(scraping.getWaitCode());
				}
				goToPage(scraping.getCode(), by);
				return _driver;
			case WAIT:
				try {
					Thread.sleep(Integer.parseInt(scraping.getCode()));
				} catch (InterruptedException ex) {
					Logger.getLogger(Operate.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			case SUB_WINDOW:
				try{
					String handle = null;
					try{
						handle = _driver.getWindowHandle();
					}catch(Exception e){}
					
					Set<String> set = _driver.getWindowHandles();
					for(String str : set){
						if(handle == null || !str.equals(handle)){
							if(handle != null){
								_driver.close();
							}
							_driver = (CustomWebDriver)_driver.switchTo().window(str);
							break;
						}
					}
				}catch(Exception e){
					throw new ScrapingFormException(ScrapingFormException.TYPE.NOT_SUB_WINDOW, "", e);
				}
				return null;
		}
		By by = findBy.getBy(scraping.getCode());
		if(by == null)	return null;
		
		TypeWebElement op = scraping.getOperate();
		
		Object param = scraping.getData();
		
		Object obj;
		try{
			switch(op.getType()){
				//引数がbyのメソッド
				case 1:
					obj = context;
					param = by;
					break;
				//セレクトボックスの場合
				case 2:
					obj = new Select(context.findElement(by));
					break;
				default:
					List<WebElement> elementList = context.findElements(by);
					if(elementList.size() == 1){
						obj = elementList.get(0);
					}else if(elementList.size() > 1){
						obj = elementList;
					}else{
						obj = context;
					}
					break;
			}
		}catch(Exception e){
			throw new ScrapingFormException(ScrapingFormException.TYPE.NOT_FOUND_ELEENT, by.toString(), e);
		}
		
		Object ret = op.exec(obj, param);
		if((op.getType() == 3) && (param != null)){
//			//遷移系のチェック
//			if(!param.equals(_driver.getCurrentUrl())){
//				throw new ScrapingDefineException(ScrapingDefineException.TYPE.JUMP_FAILD, "希望遷移先："+param+"　実際の遷移先："+_driver.getCurrentUrl(), null);
//			}
			//遷移系はdriverを返す
			ret = _driver;
		}
		return ret;
	}
	private CustomWebDriver getWebDriver(){
		return new CustomWebDriver(_enableJavascript, _requestHeader);
	}
	/**
	 * TypeWebElementのメソッドを実行
	 * @param type
	 * @param param
	 * @return
	 * @throws ScrapingException 
	 */
	public Object exec(TypeWebElement type, Object param) throws ScrapingException{
		return type.exec(_driver, param);
	}
	/**
	 * ページ遷移
	 * @param url
	 * @return
	 * @throws info.ryuojima.util.scraping.exception.ScrapingException
	 */
	public WebDriver goToPage(String url) throws ScrapingException{
		return goToPage(_driver, url, null);
	}
	/**
	 * ページ遷移
	 * @param url
	 * @param findBy	要素が出るまで待機
	 * @return
	 * @throws ScrapingException 
	 */
	public WebDriver goToPage(String url, By findBy) throws ScrapingException{
		return goToPage(_driver, url, findBy);
	}
	/**
	 * ページ遷移
	 * @param driver
	 * @param url
	 * @return
	 */
	private WebDriver goToPage(final WebDriver driver, final String url, final By findBy) throws ScrapingException{
		try{
//			((HtmlUnitDriver)driver).setJavascriptEnabled(false);
//			((HtmlUnitDriver)driver).setJavascriptEnabled(true);
			driver.navigate().to(url);
			new WebDriverWait(driver, 10, 10).until(new ExpectedCondition<WebElement>(){
				@Override
				public WebElement apply(WebDriver arg0) {
					if(findBy != null){
						return arg0.findElement(findBy);
					}
					return arg0.findElement(By.tagName("body"));
				}
			});
		}catch(Exception e){
			throw new ScrapingFormException(ScrapingFormException.TYPE.NOT_FOUND, url, e);
		}
		
		return _driver;
	}
	/**
	 * ファイルのダウンロード
	 * @param downloadUrl	ダウンロードするURL
	 * @return
	 * @throws info.ryuojima.util.scraping.exception.ScrapingFormException
	 */
	public InputStream download(String downloadUrl) throws ScrapingFormException{
		StringBuilder buf = new StringBuilder();
		Set<Cookie> cookies = _driver.manage().getCookies();
		for(Cookie cookie : cookies){
			buf.append(cookie.toString());
		}
		
		InputStream stream = null;
		try {
			URL url = new URL(downloadUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Cookie", buf.toString());
			conn.setAllowUserInteraction(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.connect();
			int httpStatusCode = conn.getResponseCode();
			if(httpStatusCode == HttpURLConnection.HTTP_OK){
				stream = conn.getInputStream();
			} 
		} catch (Exception ex) {
			throw new ScrapingFormException(ScrapingFormException.TYPE.NOT_FOUND, downloadUrl, ex);
		}
		if(stream == null){
			throw new ScrapingFormException(ScrapingFormException.TYPE.NOT_FOUND, downloadUrl, null);
		}
//		return new DataInputStream(stream);
		return stream;
	}
	/**
	 * POST送信
	 * @param postUrl
	 * @param data 
	 * @return  
	 * @throws info.ryuojima.util.scraping.exception.ScrapingFormException 
	 */
	public String post(String postUrl, Map<String,String> data, String encode) throws ScrapingFormException {
		StringBuilder buf = new StringBuilder();
		for(Map.Entry<String,String> entry : data.entrySet()){
			if(buf.length()<=0){
				buf.append("?");
			}else{
				buf.append("&");
			}
			buf.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return post(postUrl, buf.toString(), encode);
	}
	/**
	 * POST送信
	 * @param postUrl
	 * @param data 
	 * @param encode 
	 * @return  
	 * @throws info.ryuojima.util.scraping.exception.ScrapingFormException 
	 */
	public String post(String postUrl, String data, String encode) throws ScrapingFormException {
		StringBuilder ret = new StringBuilder();
		
		StringBuilder buf = new StringBuilder();
		Set<Cookie> cookies = _driver.manage().getCookies();
		for(Cookie cookie : cookies){
			buf.append(cookie.toString());
		}
		InputStreamReader input = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(postUrl);
			URLConnection conn = (HttpURLConnection) url.openConnection();
			if(conn instanceof HttpsURLConnection){
				ignoreValidateCertification((HttpsURLConnection)conn);
			}
			conn.setDoOutput(true);
			conn.addRequestProperty("Cookie", buf.toString());
			conn.addRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
			conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream os = null;
			PrintStream ps = null;
			try{
				//POST用のOutputStreamを取得
				os = conn.getOutputStream();

				ps = new PrintStream(os);
				ps.print(data);//データをPOSTする
				ps.close();
			}finally{
				//closeで送信完了
				if(os != null)	os.close();
				if(ps != null)	ps.close();
			}
			
			InputStream stream;
			try{
				//200以外は例外
				stream = conn.getInputStream();
			}catch(Exception e){
				if(conn instanceof HttpURLConnection){
					stream = ((HttpURLConnection)conn).getErrorStream();
				}else if(conn instanceof HttpsURLConnection){
					stream = ((HttpsURLConnection)conn).getErrorStream();
				}else{
					return null;
				}
			}
			if(encode != null){
				input = new InputStreamReader(stream, encode);
			}else{
				input = new InputStreamReader(stream);
			}
			reader = new BufferedReader(input);

			String line;
			while((line=reader.readLine()) != null){
				ret.append(line).append(System.getProperty("line.separator"));
			}
			
		} catch (Exception e) {
			throw new ScrapingFormException(ScrapingFormException.TYPE.NOT_FOUND, postUrl, e);
		}finally{
			if(input != null){
				try{
					input.close();
				} catch (IOException ex) {}
			}
			if(reader != null){
				try{
					reader.close();
				} catch (IOException ex) {}
			}
		}
		return ret.toString();
	}
	private static void ignoreValidateCertification(
            HttpsURLConnection httpsconnection)
            throws NoSuchAlgorithmException, KeyManagementException {
		KeyManager[] km = null;
		TrustManager[] tm = { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
								throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
								throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
						return null;
				}
		} };
		SSLContext sslcontext = SSLContext.getInstance("SSL");
		sslcontext.init(km, tm, new SecureRandom());
		httpsconnection.setSSLSocketFactory(sslcontext.getSocketFactory());
	}

	@Override
	protected void finalize() throws Throwable{
		try{
			super.finalize();
		}finally{
			if(_driver != null){
				_driver.quit();
			}
		}
	}
}
