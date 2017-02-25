package com.kerbybit.chattriggers.triggers;

public class TagHandler {
	static String eventTags(int tag, String TMP_e) {
		String returnString;
		switch (tag) {
		case 1:
			returnString = TMP_e.substring(TMP_e.indexOf("<time=")+6, TMP_e.indexOf(">",TMP_e.indexOf("<time=")));
			break;
		case 2:
			returnString = TMP_e.substring(TMP_e.indexOf("<pos=")+5, TMP_e.indexOf(">",TMP_e.indexOf("<pos=")));
			break;
		case 3:
			returnString = TMP_e.substring(TMP_e.indexOf("<vol=")+5, TMP_e.indexOf(">",TMP_e.indexOf("<vol=")));
			break;
		case 4:
			returnString = TMP_e.substring(TMP_e.indexOf("<pitch=")+7, TMP_e.indexOf(">",TMP_e.indexOf("<pitch=")));
			break;
		default:
			returnString = "";
			break;
		}
		
		return returnString;
	}
	
	public static String removeTags(String TMP_trig) {
		String TMP_list;
		TMP_trig = TMP_trig.replace("<s>", "").replace("<c>", "").replace("<e>", "")
				.replace("<start>", "").replace("<contain>", "").replace("<end>", "")
				.replace("<imported>", "").replace("<formatted>", "");
		if (TMP_trig.contains("<list=")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<list=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))); TMP_trig = TMP_trig.replace("<list="+TMP_list+">","");}
		if (TMP_trig.contains("<server=")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<server=")+8, TMP_trig.indexOf(">", TMP_trig.indexOf("<server="))); TMP_trig = TMP_trig.replace("<server="+TMP_list+">","");}
		if (TMP_trig.contains("<case=")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<case=")+6, TMP_trig.indexOf(">",TMP_trig.indexOf("<case="))); TMP_trig = TMP_trig.replace("<case="+TMP_list+">","");}

		return TMP_trig;
	}
}
