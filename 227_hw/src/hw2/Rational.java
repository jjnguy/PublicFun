package hw2;

public class Rational implements Comparable<Rational> {

	private int num, denom;
	
	public Rational(int num, int denom){
		if (denom == 0){
			this.num = this.denom = 1;
		}
		this.num = num;
		this.denom = denom;
	}
	
	public Rational(){
		this(1, 1);
	}
	
	public Rational(String str){
		int idxOfSlash = str.indexOf("/");
		num = Integer.parseInt(str.trim().substring(1, idxOfSlash).trim());
		denom = Integer.parseInt(str.substring(idxOfSlash + 1, str.length() - 1).trim());
	}
	
	public int getNum(){
		return num;
	}
	
	public int getDen(){
		return denom;
	}
	
	public Rational add(Rational other){
		Rational new1 = this.multiply(other.denom);
		Rational new2 = other.multiply(this.denom);
		return new Rational(new1.num + new2.num, new1.denom);
	}
	
	public Rational subtract(Rational other){
		Rational new1 = this.multiply(other.denom);
		Rational new2 = other.multiply(this.denom);
		return new Rational(new1.num - new2.num, new1.denom);
	}
	
	public Rational multiply(int num){
		return new Rational(this.num * num, this.denom * num);
	}
	
	public Rational multiply(Rational other){
		return new Rational(this.getNum() * other.getNum(), this.getDen() * other.getDen());
	}
	
	public Rational divide(Rational other){
		return new Rational(this.getNum() * other.getDen(), this.getDen() * other.getNum());
	}

	public int compare(Rational other){
		return this.compareTo(other);
	}
	
	public String toString(){
		if (denom == 1) return "" + num;
		if (denom == num) return "1";
		return num + "/" + denom;
	}
	
	@Override
	public int compareTo(Rational other) {
		return (int)((this.num / (double)this.denom) - (other.num / (double)other.num));
	}	
}
