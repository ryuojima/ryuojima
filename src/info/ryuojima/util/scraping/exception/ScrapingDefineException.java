/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.ryuojima.util.scraping.exception;

/**
 * スクレイピング 指定エラー
 * @author SYS-OJIMA13
 */
public class ScrapingDefineException extends ScrapingException{
	public enum TYPE{
		NONE_TYPE_FIND_BY(								1001,					"TypeFindByの指定がありません。指定は必須です。"),							//TypeFindByの指定がない
		NOT_FOUND_TYPE_FIND_BY(						1002,					"定義されていないTypeFindByが指定されました。(@1)"),						//TypeFindByのcodeが不正
		NONE_TYPE_FIND_BY_CODE(						1003,					"TypeFindByの指定がURL以外の場合、codeの指定は必須です。"),			//TypeFindByがURL以外の場合はcodeの指定が必須
		NONE_TYPE_WEB_ELEMENT(						1101,					"TypeWebElementの指定がありません。指定は必須です。"),					//TypeWebElementの指定がない
		NOT_FOUND_TYPE_WEB_ELEMENT(				1102,					"定義されていないTypeWebElementが指定されました。(@1)"),				//TypeWebElementのcodeが不正
		DISUSED_PARAM_TYPE_WEB_ELEMENT(		1103,					"@1はdataは不要です。"),																		//TypeWebElementのメソッドの実行に引数は不要
		REQUIRED_PARAM_TYPE_WEB_ELEMENT(	1104,					"@1はdataは必須です。"),																		//TypeWebElementのメソッドの実行に引数は必須
		JUMP_FAILD(												1105,					"ページ遷移先が正しく有りません。(@1)");											//遷移系のメソッドを実行して指定のURLにジャンプできなかった
		
		private final int			_code;
		private final String	_message;
		private TYPE(int code, String message){
			this._code = code;
			this._message = message;
		}
		public int getCode(){
			return this._code;
		}
		public String getMessage(){
			return this._message;
		}
	};
	
	public ScrapingDefineException(TYPE type, Throwable throwable){
		super(type.getMessage(), throwable);
	}
	public ScrapingDefineException(TYPE type, String msg1, Throwable throwable){
		super(type.getMessage().replaceAll("@1", msg1), throwable);
	}
}
