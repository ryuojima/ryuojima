///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package info.ryuojima.util.scraping;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//
///**
// *
// * @author SYS-OJIMA13
// */
//public class ScrapingMap implements Map,Serializable{
//	private Map<String,Object>	map			= new TreeMap<String,Object>();
//	private List<ScrapingMap>		child;
//
//	public List<ScrapingMap> getChild() {
//		
//		return child;
//	}
//	public void setChild(List<ScrapingMap> child) {
//		this.child = child;
//	}
//	public void addChild(ScrapingMap child){
//		if(this.child == null){
//			this.child = new ArrayList<ScrapingMap>();
//		}
//		this.child.add(child);
//	}
//
//	public int compare(Object k1, Object k2) {
//			return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
//					: comparator.compare((K)k1, (K)k2);
//	}
//	@Override
//	public int size() {
//		return map.size();
//	}
//	@Override
//	public boolean isEmpty() {
//		return map.isEmpty();
//	}
//	@Override
//	public boolean containsKey(Object key) {
//		return map.containsKey(key);
//	}
//	@Override
//	public boolean containsValue(Object value) {
//		return map.containsValue(value);
//	}
//	@Override
//	public Object get(Object key) {
//		return map.get(key);
//	}
//	@Override
//	public Object put(Object key, Object value) {
//		return map.put(null, value);
//	}
//	@Override
//	public Object remove(Object key) {
//		return map.remove(key);
//	}
//	@Override
//	public void putAll(Map m) {
//		map.putAll(m);
//	}
//	@Override
//	public void clear() {
//		map.clear();
//	}
//	@Override
//	public Set keySet() {
//		return map.keySet();
//	}
//	@Override
//	public Collection values() {
//		return map.values();
//	}
//	@Override
//	public Set entrySet() {
//		return map.entrySet();
//	}
//}
