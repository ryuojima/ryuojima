package info.ryuojima.util.scraping;

import java.io.Serializable;

import info.ryuojima.util.scraping.type.TypeFindBy;
import info.ryuojima.util.scraping.type.TypeWebElement;
import java.util.ArrayList;
import java.util.List;

public class ScrapingDriver implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 子
	 */
	private List<ScrapingDriver>	child;
	
	/**
	 * TypeFindByで実行する引数、またはURL
	 */
	private String								code;
	/**
	 * Byで実行するEnum
	 */
	private TypeFindBy						findBy;
	/**
	 * WebElementまたはSelectで実行するEnum
	 */
	private TypeWebElement				operate;
	/**
	 * TypeWebElementで実行する引数
	 */
	private String								data;
	/**
	 * データ取得時のキー名称
	 */
	private String								key;
	
	/**
	 * URL遷移時に指定の要素があるまでWAITを入れる
	 */
	private String								waitCode;
	private TypeFindBy						waitBy;
	
	public ScrapingDriver(String code, TypeFindBy findBy,
													TypeWebElement operate, String data, String key) {
		super();
		this.code = code;
		this.findBy = findBy;
		this.operate = operate;
		this.data = data;
		this.key = key;
	}
	public ScrapingDriver(String code, TypeFindBy findBy,
													TypeWebElement operate, String data, String key, String waitCode, TypeFindBy waitBy) {
		super();
		this.code = code;
		this.findBy = findBy;
		this.operate = operate;
		this.data = data;
		this.key = key;
		this.waitCode = waitCode;
		this.waitBy = waitBy;
	}

	public List<ScrapingDriver> getChild() {
		return child;
	}

	public void setChild(List<ScrapingDriver> child) {
		this.child = child;
	}
	
	public void addChild(ScrapingDriver child){
		if(this.child == null){
			this.child = new ArrayList<>();
		}
		this.child.add(child);
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public TypeFindBy getFindBy() {
		return findBy;
	}
	public void setFindBy(TypeFindBy findBy) {
		this.findBy = findBy;
	}
	public TypeWebElement getOperate() {
		return operate;
	}
	public void setOperate(TypeWebElement operate) {
		this.operate = operate;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getWaitCode() {
		return waitCode;
	}
	public void setWaitCode(String waitCode) {
		this.waitCode = waitCode;
	}
	public TypeFindBy getWaitBy() {
		return waitBy;
	}
	public void setWaitBy(TypeFindBy waitBy) {
		this.waitBy = waitBy;
	}
}
