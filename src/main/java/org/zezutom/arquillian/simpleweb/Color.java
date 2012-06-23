package org.zezutom.arquillian.simpleweb;

public enum Color {
	RED("red"),
	GREEN("green"),
	BLUE("blue"),
	GREY("grey");
	
	private String value;
	
	private Color(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
