package dev.dietermai.projectselect.internal;

public class InitialsSearchPattern{
	
	boolean checkForInitalMatch(String pattern, String text) {
 		return sanity(pattern, text) && isInitialsPattern(pattern) && isMatchOfInitialPattern(pattern, text);
	}
	
	private boolean sanity(String pattern, String text) {
		if(pattern == null) {
			return false;
		}else if(text == null) {
			return false;
		}else if(pattern.isEmpty()) {
			return false;
		}else if(text.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	private boolean isInitialsPattern(String pattern) {
		for(int i = 0; i < pattern.length(); i++) {
			if( !Character.isAlphabetic(pattern.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isMatchOfInitialPattern(String pattern, String text) {
		int textIndex = 0;

		char initialSearchedFor;
		char initialOfText;
		
		
		for(int patternIndex = 0; patternIndex < pattern.length(); patternIndex++) {
			textIndex = getNextInitial(text, textIndex);
			if(textIndex == -1) {
				return false;
			}
			
			initialSearchedFor = Character.toLowerCase(pattern.charAt(patternIndex));
			initialOfText = Character.toLowerCase(text.charAt(textIndex));
			if( initialSearchedFor != initialOfText ) {
				return false;
			}else if(textIndex+1 >= text.length()){
				return false;
			}else{
				textIndex++;
			}
		}
		
		return true;
	}
	
	private int getNextInitial(String text, int startIndex) {
		if(startIndex == 0) {
			for(int i = 0; i < text.length(); i++) {
				if(Character.isAlphabetic(text.charAt(i))) {
					return i;
				}
			}
		}else if(Character.isUpperCase(text.charAt(startIndex))){
			return startIndex;
		}else {
			for(int i = startIndex; i < text.length(); i++) {
				if(!Character.isAlphabetic(text.charAt(i-1)) && Character.isAlphabetic(text.charAt(i))) {
					return i;
				}else if(Character.isAlphabetic(text.charAt(i-1)) && Character.isLowerCase(text.charAt(i-1)) && Character.isUpperCase(text.charAt(i))){
					return i;
				}
			}
		}
		return -1;
	}

}
