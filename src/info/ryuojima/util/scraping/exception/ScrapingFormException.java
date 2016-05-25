/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.ryuojima.util.scraping.exception;

/**
 * スクレイピング　形式エラー
 * @author SYS-OJIMA13
 */
public class ScrapingFormException extends ScrapingException{
	public enum TYPE{
		NOT_FOUND(												2001,			"指定のURLが存在しません。(@1)"),
		INVOKE_BY_EXCEPTION(							2101,			"「org.openqa.selenium.By」の@1実行でエラーがあります。"),
		INVOKE_WEB_ELEMENT_EXCEPTION(			2201,			"「org.openqa.selenium.WebElement」の@1実行でエラーがあります。"),
		NOT_FOUND_ELEENT(									2301,			"エレメントが存在しません。(@1)"),
		NOT_SUB_WINDOW(										2501,			"サブウィンドウが存在しません。(@1)");
		
		private final int				_code;
		private final String		_message;
		private TYPE(int code, String message){
			this._code = code;
			this._message = message;
		}
		public int getCode(){
			return _code;
		}
		public String getMessage(){
			return _message;
		}
	};
	private final TYPE _type;
	
	public ScrapingFormException(TYPE type, Throwable throwable){
		super(type.getMessage().replaceAll("@1", "???"), throwable);
		this._type = type;
	}
	public ScrapingFormException(TYPE type, String msg1, Throwable throwable){
		super(msg1 != null ? type.getMessage().replaceAll("@1", msg1) : "", throwable);
		this._type = type;
	}
	public TYPE getType(){
		return _type;
	}
}
