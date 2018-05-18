package util;

public final class StringConverter {
	private StringConverter() {
		
	}
	
	public static String arrayToString(String[] strings, String separator) {
		StringBuilder sB = new StringBuilder();
		for(int i=0; i<strings.length; i++) {
			if (i>0) {
				sB.append(separator + " ");
			}
			sB.append(strings[i]);
		}
		return sB.toString();
	}
	
	public static String arrayToString(String[] strings) {
		return arrayToString(strings, ",");
	}
}
