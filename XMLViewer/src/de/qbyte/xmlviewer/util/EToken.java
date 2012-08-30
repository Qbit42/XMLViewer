package de.qbyte.xmlviewer.util;

public enum EToken {

	/* ***** ENUMERATION ***** */

	EOF("End of File"),

	ATTRIBUTE_EQUAL_SIGNS("Attribute Equal Signs"),

	ATTRIBUTE_NAMES("Attribute Names"),

	ATTRIBUTE_VALUES("Attribute Values"),
	
	CDATA_CONTENT("CDATA Content"),
	
	CDATA_DELIMITERS("CDATA Delimiters"),
	
	COMMENT_CONTENT("Comment Content"),
	
	COMMENT_DELIMITERS("Comment Delimiters"),
	
	CONTENT("Content"),
	
	DECLARATION_DELIMITERS("Declaration Delimiters"),
	
	DOCTYPE_NAME("DOCTYPE Name"),
	
	DOCTYPE_PUBLIC_REFERENCE("DOCTYPE Public Reference"),
	
	DOCTYPE_SYSTEM_REFERENCE("DOCTYPE System Reference"),
	
	DOCTYPE_KEYWORD("DOCTYPE SYSTEM/PUBLIC Keyword"),
	
	ENTITY_REFERENCES("Entity References"),
	
	PROCESSING_INSTRUCTION_CONTENT("Processing Instruction Content"),

	PROCESSING_INSTRUCTION_DELIMITERS("Processing Instruction Delimiters"),

	TAG_DELIMITERS("Tag Delimiters"),
	
	TAG_NAMES("Tag Names"),

	WHITESPACE("Whitespace"),

	OTHER("Other");

	/* ***** PROPERTIES ***** */

	private String	name;

	/* ***** CONSTRUCTORS ***** */

	private EToken(String name) {
		this.setName(name);
	}

	/* ***** GETTER/SETTER ***** */

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
