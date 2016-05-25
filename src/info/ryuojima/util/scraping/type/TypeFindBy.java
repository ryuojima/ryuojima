package info.ryuojima.util.scraping.type;

import java.lang.reflect.Method;

import org.openqa.selenium.By;
import info.ryuojima.util.scraping.exception.ScrapingDefineException;
import info.ryuojima.util.scraping.exception.ScrapingException;
import info.ryuojima.util.scraping.exception.ScrapingFormException;

public enum TypeFindBy {
	URL(									0,			null								),			//指定したURLへ移動
	ID(									1,			"id"								),			//指定したid属性を検索条件とする
	NAME(								2,			"name"							),			//指定したname属性を検索条件とする
	CLASS_NAME(					3,			"className"					),			//指定したクラス名(class属性)を検索条件とする
	TAG_NAME(						4,			"tagName"						),			//指定したタグ名を検索条件とする
	LINK_TEXT(					5,			"linkText"					),			//指定したリンク名(完全一致)を検索条件とする
	PARTIAL_LINK_TEXT(	6,			"partialLinkText"		),			//指定したリンク名(部分一致)を検索条件とする
	CSS_SELECTOR(				7,			"cssSelector"				),			//指定したCSSセレクタを検索条件とする
	XPATH(							8,			"xpath"							),			//指定したXPathを検索条件とする
	SUB_WINDOW(					100,		null								),			//サブウィンドウに切り替える
	WAIT(								9999,		null								);			//WAITの実行
	
	private final int			_id;
	private final String	_method;
	
	private TypeFindBy(int id, String method){
		this._id = id;
		this._method = method;
	}
	public int getId(){
		return _id;
	}
	/**
	 * Byの取得
	 * @param arg
	 * @return
	 * @throws info.ryuojima.util.scraping.exception.ScrapingException
	 */
	public By getBy(String arg) throws ScrapingException{
		By by = null;
		if(this._method != null){
			if(arg == null || arg.trim().length()<=0){
				throw new ScrapingDefineException(ScrapingDefineException.TYPE.NONE_TYPE_FIND_BY_CODE, null);
			}
			
			try{
				Method method = By.class.getMethod(this._method, new Class[]{String.class});
				by = (By)method.invoke(null, new Object[]{arg});
			}catch(Exception e){
				throw new ScrapingFormException(ScrapingFormException.TYPE.INVOKE_BY_EXCEPTION, this._method, e);
			}
		}
		return by;
	}
	public static TypeFindBy getEnum(int id) throws ScrapingException{
		TypeFindBy ret = null;
		
		TypeFindBy[] enums = TypeFindBy.values();
		for (TypeFindBy e : enums) {
			if(e._id == id){
				ret = e;
				break;
			}
		}
		if(ret == null){
			throw new ScrapingDefineException(ScrapingDefineException.TYPE.NOT_FOUND_TYPE_FIND_BY, String.valueOf(id), null);
		}
		return ret;
	}
}
