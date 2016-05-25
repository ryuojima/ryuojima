package info.ryuojima.util.scraping.type;

import java.util.List;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import info.ryuojima.util.scraping.exception.ScrapingDefineException;
import info.ryuojima.util.scraping.exception.ScrapingException;
import info.ryuojima.util.scraping.exception.ScrapingFormException;
import java.util.ArrayList;

/**
 * WebElement
 * @author ryu
 *
 */
public enum TypeWebElement {
	//WebElement
	CLEAR(											1,		0,	"clear",									null,									null							),		//テキスト入力要素の値をクリア
	CLICK(											2,		3,	"click",									null,									null							),		//要素をクリック
	FIND_ELEMENT(								3,		1,	"findElement",						By.class,							WebElement.class	),		//条件に一致する最初の要素を検索
	FIND_ELEMENTS(							4,		1,	"findElements",						By.class,							List.class				),		//条件に一致する要素を検索
	GET_ATTRIBUTE(							5,		0,	"getAttribute",						String.class,					String.class			),		//要素の属性値を取得
	GET_CSS_VALUE(							6,		0,	"getCssValue",						String.class,					String.class			),		//CSSプロパティを取得
	GET_TAG_NAME(								7,		0,	"getTagName",							null,									String.class			),		//要素のタグ名を取得
	GET_TEXT(										8,		0,	"getText",								null,									String.class			),		//要素のテキストを取得
	IS_DISPLAYED(								9,		0,	"isDisplayed",						null,									Boolean.class			),		//要素が表示されているか否かを取得
	IS_ENAGLED(									10,		0,	"isEnabled",							null,									Boolean.class			),		//要素が現在有効か否かを取得
	IS_SELECTED(								11,		0,	"isSelected",							null,									Boolean.class			),		//要素が選択されているか否かを取得
	SEND_KEYS(									12,		0,	"sendKeys",								CharSequence[].class,	null							),		//タイピングのシミュレート
	SUBMIT(											13,		3,	"submit",									null,									null							),		//フォームをサブミット
	//Select
	SELECT_DESELECT_ALL(					101,	2,	"deselectAll",						null,									null							),		//すべての選択状態をクリアする。
	SELECT_DESELECT_BY_INDEX(		102,	2,	"deselectByIndex",				Integer.class,				null							),		//指定したインデックス番号の選択をクリアする。
	SELECT_DESELECT_BY_VALUE(		103,	2,	"deselectByValue",				String.class,					null							),		//指定した値(value属性の値)の選択をクリアする。
	SELECT_DESELECT_BY_VISIBLE(	104,	2,	"deselectByVisibleText",	String.class,					null							),		//指定した表示テキストの選択をクリアする。
	SELECT_SELECT_BY_INDEX(			105,	2,	"selectByIndex",					Integer.class,				null							),		//指定したインデックス番号を選択する。
	SELECT_SELECT_BY_VALUE(			106,	2,	"selectByValue",					String.class,					null							),		//指定した値(value属性の値)を選択する。
	SELECT_SELECT_BY_VISIBLE(		107,	2,	"selectByVisibleText",		String.class,					null							),		//指定した表示テキストを選択する。
	SELECT_GET_SELECTED(				108,	2,	"getAllSelectedOptions",	null,									List.class				),		//選択中のオプション(項目)を取得
	SELECT_GET_FIRST_SELECTED(	109,	2,	"getFirstSelectedOption",	null,									WebElement.class	),		//一番最初の選択中のオプション(項目)を取得
	SELECT_GET_OPTION(					110,	2,	"getOptions",							null,									List.class				),		//全てのオプション(項目)を取得
	SELECT_IS_MULTIPLE(					111,	2,	"isMultiple",							null,									Boolean.class			);		//複数選択が可能か？
	
	private final int				_id;
	private final int				_type;
	private final String		_method;
	private final Class<?>	_param;
	private final Class<?>	_ret;
	
	/**
	 * コンストラクタ
	 * @param id				ID
	 * @param type			実行するメソッドのクラスタイプ（0:WebDriver、1:Select）
	 * @param method		メソッド名
	 * @param param			パラメータのクラス
	 * @param ret				引数のクラス
	 */
	private TypeWebElement(int id, int type, String method, Class<?> param, Class<?> ret){
		this._id = id;
		this._type = type;
		this._method = method;
		this._param = param;
		this._ret = ret;
	}
	public int getId(){
		return _id;
	}
	public int getType(){
		return _type;
	}
	/**
	 * パラメータ必須か？
	 * @return 
	 */
	public boolean requiredParameter(){
		return (_param != null);
	}
	/**
	 * 戻り値は存在するか？
	 * @return 
	 */
	public boolean haveReternParameter(){
		return (_ret != null);
	}
	/**
	 * パラメータのチェック
	 * @param parameter
	 * @throws ScrapingException 
	 */
	public void checkParameter(Object parameter) throws ScrapingException{
		//遷移系はチェックしない
		if(this._type == 3)		return;
		
		if(this._param != null && parameter == null){
			throw new ScrapingDefineException(ScrapingDefineException.TYPE.REQUIRED_PARAM_TYPE_WEB_ELEMENT, this._method, null);
		}else if(this._param == null && parameter != null){
			throw new ScrapingDefineException(ScrapingDefineException.TYPE.DISUSED_PARAM_TYPE_WEB_ELEMENT, this._method, null);
		}
	}
	/**
	 * メソッドの実行
	 * @param obj			実行するクラス
	 * @param param		実行時の引数
	 * @return				戻り値
	 * @throws ScrapingException 
	 */
	public Object exec(Object obj, Object param) throws ScrapingException{
		Object ret = null;
		
		//パラメータチェック
		checkParameter(param);
		
		if(obj instanceof List){
			List objList = (List)obj;
			for (Object objList1 : objList) {
				Object retObj = execObject(objList1, param);
				if(retObj != null){
					if(ret == null){
						ret = new ArrayList();
					}
					((List)ret).add(retObj);
				}
			}
		}else{
			ret = execObject(obj, param);
		}
		return ret;
	}
	private Object execObject(Object obj, Object param) throws ScrapingException{
		Object ret = null;
		
		if(this.equals(TypeWebElement.SEND_KEYS)){
			//引数の「CharSequence[]」の渡しがうまくいかないのでここだけハードコーディング
			try{
				((WebElement)obj).sendKeys((String)param);
			}catch(Exception e){
				throw new ScrapingFormException(ScrapingFormException.TYPE.NOT_FOUND_ELEENT, "", e);
			}
		}else if(this._method != null){
			try{
				Method method;
				Class<?>[] classs = null;
				Object[] params = null;
				if(_param != null){
					classs = new Class[]{_param};
					params = new Object[]{param};
				}
				
				method = obj.getClass().getMethod(this._method, classs);
				if(_ret != null){
					ret = method.invoke(obj, params);
				}else{
					method.invoke(obj, params);
				}
			}catch(Exception e){
				throw new ScrapingFormException(ScrapingFormException.TYPE.INVOKE_WEB_ELEMENT_EXCEPTION, this._method, e);
			}
		}
		return ret;
	}
	public static TypeWebElement getEnum(Integer id) throws ScrapingException{
		TypeWebElement ret = null;
		if(id == null)		return ret;
		
		TypeWebElement[] enums = TypeWebElement.values();
		for (TypeWebElement e : enums) {
			if(e._id == id){
				ret = e;
				break;
			}
		}
		if(ret == null){
			throw new ScrapingDefineException(ScrapingDefineException.TYPE.NONE_TYPE_WEB_ELEMENT, String.valueOf(id), null);
		}
		return ret;
	}
}
