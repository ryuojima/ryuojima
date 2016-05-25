/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.ryuojima.util.scraping.exception;

/**
 * スクレイピング関連の例外クラス
 * @author SYS-OJIMA13
 */
public class ScrapingException extends Exception{
	public ScrapingException(){}
	
	public ScrapingException(ScrapingException ex, int raw){
		super(ex.getMessage() + "　実行順：["+raw+"]", ex.getCause());
	}
	public ScrapingException(String message, Throwable throwable){
		super(message, throwable);
	}
}
