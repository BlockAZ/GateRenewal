package model;

public enum ItemGroup {
	ALL("all"), 
	BOARD("board"),
	WELDING("welding"),
	OTHER("other");
	
	private final String text;
	
	ItemGroup(final String text) {
        this.text = text;
    }
	
	public String toString() {
        return text;
    }
}
