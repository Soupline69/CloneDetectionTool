package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
	private int begin;
	private int end;
	private int size;
	private int suffixLink;
	private Map<CustomToken, Integer> sons;
	private List<Integer> set;
	
	public Node(int begin, int end, int size) {
		this.begin = begin;
		this.end = end;
		this.size = size;
		suffixLink = -1;
		sons = new HashMap<>();
		set = new ArrayList<>();
	}
	
	public void addSet(int element) {
		set.add(element);
	}
	
	public List<Integer> getSet() {
		return set;
	}
	
	public void setSet(List<Integer> set) {
		this.set = set;
	}
	
	public int getBegin() {
		return begin;
	}
	
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getSuffixLink() {
		return suffixLink;
	}
	
	public void setSuffixLink(int suffixLink) {
		this.suffixLink = suffixLink;
	}
	
	public Map<CustomToken, Integer> getSons() {
		return sons;
	}

}