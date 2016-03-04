package com.kerbybit.chattriggers;

public class TagHandler {
	public static String eventTags(int tag, String TMP_e) {
		String returnString = "";
		switch (tag) {
		case 1:
			if (TMP_e.contains("<time=") && TMP_e.contains(">")) {returnString = TMP_e.substring(TMP_e.indexOf("<time=")+6, TMP_e.indexOf(">",TMP_e.indexOf("<time=")));}
			break;
		case 2:
			if (TMP_e.contains("<pos=") && TMP_e.contains(">")) {returnString = TMP_e.substring(TMP_e.indexOf("<pos=")+5, TMP_e.indexOf(">",TMP_e.indexOf("<pos=")));}
			break;
		case 3:
			if (TMP_e.contains("<vol=") && TMP_e.contains(">")) {returnString = TMP_e.substring(TMP_e.indexOf("<vol=")+5, TMP_e.indexOf(">",TMP_e.indexOf("<vol=")));}
			break;
		case 4:
			if (TMP_e.contains("<pitch=") && TMP_e.contains(">")) {returnString = TMP_e.substring(TMP_e.indexOf("<pitch=")+7, TMP_e.indexOf(">",TMP_e.indexOf("<pitch=")));}
			break;
		default:
			returnString = "";
			break;
		}
		
		return returnString;
	}
}
