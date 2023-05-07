package com.mcfan.messages;


public class Builder {
	
	private StringBuilder stringBuilder;
	
	public Builder() {
		stringBuilder = new StringBuilder();
	}
	
	public Builder(String message) {
		stringBuilder = new StringBuilder(message);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Remove all contained text. 
	 */
	public Builder clear() {
		stringBuilder.setLength(0);
		return this;
	}

	/**
	 * Create new instance of StringBuilder if is null or remove all contained text and replace with new text.
	 * 
	 * @param newMessage text to be set
	 * 
	 * @return The resulting Builder
	 */
	public Builder setBuilder(String newMessage) {
		if(stringBuilder == null) {
			stringBuilder = new StringBuilder(newMessage);
			return this;
		}
		clear();
		stringBuilder.append(newMessage);
		return this;
	}
	
	/**
	 * Sets the entire contents of the StringBuilder to upper case.
	 * 
	 * @return The resulting Builder
	 */
	public Builder toUpperCase() {
		for(int i = 0 ; i < stringBuilder.length();i++) {
			stringBuilder.setCharAt(i, Character.toUpperCase(stringBuilder.charAt(i)));
		}
		return this;
	}
	
	/**
	 * Sets the entire contents of the StringBuilder to lower case.
	 * 
	 * @return The resulting Builder
	 */
	public Builder toLowerCase() {
		for(int i = 0 ; i < stringBuilder.length();i++) {
			stringBuilder.setCharAt(i, Character.toLowerCase(stringBuilder.charAt(i)));
		}
		return this;
	}
	
	/**
	 * The character at the specified index is set to ch. 
	 * This sequence is altered to represent a new character sequence that is identical to the old character sequence, except that it contains the character ch at position index. 
	 * <p>
	 * The index argument must be greater than or equal to 0, and less than the length of this sequence.
	 * @param index the index of the character to modify.
	 * @param ch the new character.
	 * 
	 * @return The resulting Builder
	 */
	public Builder setCharAt(int index, char ch) {
		stringBuilder.setCharAt(index, ch);
		return this;
	}
	
	/**
	 * Returns the char value in this sequence at the specified index. The first char value is at index 0, the next at index 1, and so on, as in array indexing. 
	 * <p>
	 * The index argument must be greater than or equal to 0, and less than the length of this sequence. 
	 * <p> 
	 * If the char value specified by the index is a surrogate, the surrogate value is returned.
	 * 
	 * @param index the index of the desired char value.
	 * 
	 * @return the char value at the specified index.
	 */
	public char charAt(int index) {
		return stringBuilder.charAt(index);
	}
	
	/**
	 * Replaces each substring of this Builder that matches the literal target sequence with the specified literal 
	 * replacement sequence. The replacement proceeds from the beginning of the string to the end.
	 * <p>
	 * For example: <p>
	 * replacing "aa" with "b" in the string "aaa" will result in"ba" rather than "ab".
	 * 
	 * @param target The sequence of char values to be replaced
	 * @param replacment The sequence of char values to be replaced
	 * 
	 * @return The resulting Builder
	 */
	public Builder replace(String target, String replacment) {
		int startIndex = stringBuilder.indexOf(target);
		
		while (startIndex != -1) {
			int endIndex = startIndex + target.length();
			stringBuilder.replace(startIndex, endIndex, replacment);
			startIndex = stringBuilder.indexOf(target);
		}
		return this;
	}
	
	/**
	 * Replaces each substring of this Builder that matches the literal target sequence with the specified literal 
	 * replacement sequence. The replacement proceeds from the beginning of the string to the end.
	 * <p>
	 * For example: <p>
	 * replacing "aa" with "b" in the string "aaa" will result in"ba" rather than "ab".
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a string by the method {@link String#valueOf(boolean)},
     * and the characters of that string were then replace target sequence.
     * 
	 * @param target The sequence of char values to be replaced
	 * @param replacment The boolean values to be replaced
	 * 
	 * @return The resulting Builder
	 */
	public Builder replace(String target, boolean replacment) {
		replace(target, String.valueOf(replacment));
		return this;
	}
	
	/**
	 * Replaces each substring of this Builder that matches the literal target sequence with the specified literal 
	 * replacement sequence. The replacement proceeds from the beginning of the string to the end.
	 * <p>
	 * For example: <p>
	 * replacing "aa" with "b" in the string "aaa" will result in"ba" rather than "ab".
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a string by the method {@link String#valueOf(int)},
     * and the characters of that string were then replace target sequence.
     * 
	 * @param target The sequence of char values to be replaced
	 * @param replacment The integer values to be replaced
	 * 
	 * @return The resulting Builder
	 */
	public Builder replace(String target, int replacment) {
		replace(target, String.valueOf(replacment));
		return this;
	}
	
	/**
	 * Replaces each substring of this Builder that matches the literal target sequence with the specified literal 
	 * replacement sequence. The replacement proceeds from the beginning of the string to the end.
	 * <p>
	 * For example: <p>
	 * replacing "aa" with "b" in the string "aaa" will result in"ba" rather than "ab".
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a string by the method {@link String#valueOf(long)},
     * and the characters of that string were then replace target sequence.
     * 
	 * @param target The sequence of char values to be replaced
	 * @param replacment The long values to be replaced
	 * 
	 * @return The resulting Builder
	 */
	public Builder replace(String target, long replacment) {
		replace(target, String.valueOf(replacment));
		return this;
	}
	
	/**
	 * Replaces each substring of this Builder that matches the literal target sequence with the specified literal 
	 * replacement sequence. The replacement proceeds from the beginning of the string to the end.
	 * <p>
	 * For example: <p>
	 * replacing "aa" with "b" in the string "aaa" will result in"ba" rather than "ab".
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a string by the method {@link String#valueOf(double)},
     * and the characters of that string were then replace target sequence.
     * 
	 * @param target The sequence of char values to be replaced
	 * @param replacment The double values to be replaced
	 * 
	 * @return The resulting Builder
	 */
	public Builder replace(String target, double replacment) {
		replace(target, String.valueOf(replacment));
		return this;
	}
	
	/**
	 * Replaces each substring of this Builder that matches the literal target sequence with the specified literal 
	 * replacement sequence. The replacement proceeds from the beginning of the string to the end.
	 * <p>
	 * For example: <p>
	 * replacing "aa" with "b" in the string "aaa" will result in"ba" rather than "ab".
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a string by the method {@link String#valueOf(float)},
     * and the characters of that string were then replace target sequence.
     * 
	 * @param target The sequence of char values to be replaced
	 * @param replacment The float values to be replaced
	 * 
	 * @return The resulting Builder
	 */
	public Builder replace(String target, float replacment) {
		replace(target, String.valueOf(replacment));
		return this;
	}
	
	/**
	 * Replaces each substring of this Builder that matches the literal target sequence with the specified literal 
	 * replacement sequence. The replacement proceeds from the beginning of the string to the end.
	 * <p>
	 * For example: <p>
	 * replacing "aa" with "b" in the string "aaa" will result in"ba" rather than "ab".
	 * <p>
	 * The overall effect is exactly as if the argument were converted to a string by the method {@link Number#toString()},
     * and the characters of that string were then replace target sequence.
     * 
	 * @param target The sequence of char values to be replaced
	 * @param replacment The numeric values to be replaced
	 * 
	 * @return The resulting Builder
	 */
	public Builder replace(String target, Number replacment) {
		replace(target, replacment.toString());
		return this;
	}
	
	public Builder append(String str) {
		stringBuilder.append(str);
		return this;
	}
	
	public Builder append(Number num) {
		stringBuilder.append(num);
		return this;
	}
	
	@Override
	public String toString() {
		return stringBuilder.toString();
	}
	
	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}
}
