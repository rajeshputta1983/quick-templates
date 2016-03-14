package com.codequicker.quick.templates.data;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;

public class NodeListIterator implements Iterator<Node> {

	private NodeList nodeList=null;
	private int end=-1;
	
	private int current=-1;
	
	public NodeListIterator(NodeList nodeList) {
		this.nodeList=nodeList;
		
		if(this.nodeList.getLength()>0)
		{
			this.end=this.nodeList.getLength()-1;
		}
	}
	
	public boolean hasNext() {
		return current<end;
	}
	
	public Node next() {
		current++;
		return this.nodeList.item(current);
	}
	
	public void remove() {
		throw new TemplateRuntimeException("not allowed to alter node list state...");
	}
}
