public enum X_O {
	X("X"), O("O"), NONE("NONE");
	
	private String name;
	
	private X_O(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
