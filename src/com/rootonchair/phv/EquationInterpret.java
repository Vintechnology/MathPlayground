package com.rootonchair.phv;
import java.util.HashMap;
import java.util.Map;

public class EquationInterpret {
	private int pos=-1
				,ch;
	private String src,orgSrc;
	private Map<String,Double> map;
	private String keyBind;
	public EquationInterpret(String src,String keyBind,double defaultValue){
		this.orgSrc=src;
		this.keyBind=keyBind;
		this.map=new HashMap<String,Double>();
		map.put(keyBind, defaultValue);
	}
	public void setString(String newSrc){
		if(newSrc!=null && !newSrc.equals(orgSrc))
			this.orgSrc=newSrc;
	}
	public void setVariable(double newValue){
		map.put(keyBind, newValue);
	}
	private void replaceVariable(){
		src=orgSrc.replace(keyBind, "("+String.valueOf(map.get(keyBind)+")"));
	}
	private void nextChar(){
		ch=(++pos<src.length()?src.charAt(pos):-1);
	}
	private boolean eat(int charToEat){
		while(ch==' ')
			nextChar();
		if(ch==charToEat){
			nextChar();
			return true;
		}
		return false;
	}
	
	public double parse() throws RuntimeException{
		replaceVariable();
		this.pos=-1;
		nextChar();
		if(ch==-1)
			return 0;
		double x= parseExpression();
		if(this.pos<src.length()) throw new RuntimeException("Unexpected character "+(char)ch);
		return x;
	}
	
	private double parseExpression() throws RuntimeException{
		double x=parseTerm();
		for(;;){
			if (eat('+'))
				x+=parseTerm();
			else if (eat('-'))
				x-=parseTerm();
			else
				return x;
		}
	}
	
	private double parseTerm()throws RuntimeException{
		double x= parseFactor();
		for(;;){
			if(eat('*'))
				x*=parseFactor();
			else if(eat('/'))
				x/=parseFactor();// may cause divided by zero
			else
				return x;
		}
	}
	
	private double parseFactor() throws RuntimeException{
		if(eat('+'))
			return parseFactor();
		else if(eat('-'))
			return -parseFactor();
		
		double x;
		int startPos=this.pos;
		if(eat('(')){
			x=parseExpression();
			if(!eat(')'))
				throw new RuntimeException("Cannot find \")\" ");// TODO: catch exception if can't find ')'
		}else if(ch>='0'&& ch<='9' || ch=='.'){
			while(ch>='0'&& ch<='9' || ch=='.')
				nextChar();
			x=Double.parseDouble(src.substring(startPos, this.pos));
		}else if (ch>='a' && ch<='z'){
			while(ch>='a' && ch<='z')
				nextChar();
			String functn=src.substring(startPos, this.pos);
			x=parseFactor();
			if (functn.equals("sqrt"))
				x=Math.sqrt(x);
			else if(functn.equals("sin"))
				x=Math.sin(Math.toRadians(x));
			else if(functn.equals("cos"))
				x=Math.cos(Math.toRadians(x));
			else if(functn.equals("tan"))
				x=Math.tan(Math.toRadians(x));
			else if(functn.equals("cot"))
				x=1/Math.tan(Math.toRadians(x));
			else
				throw new RuntimeException("Unknown function "+functn);
		}else
			throw new RuntimeException("Unexpected character "+(char)ch);
		
		if(eat('^')){
			double y=parseFactor();
			x=Math.pow(x, y);
		}
		return x;
	}
}
