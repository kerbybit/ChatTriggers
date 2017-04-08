package com.kerbybit.chattriggers.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.ArrayHandler;

///TODO DEPRECATED
public class JsonHandler {
    public static HashMap<String, String> jsonURL = new HashMap<String, String>();

	public static String exportJsonFile(String fileName, String arrayName, String nodeName) throws IOException {
		StringBuilder returnString = new StringBuilder();
		int arrayNum = -1;
		
		for (int i = 0; i< ArrayHandler.getArraysSize(); i++) {
			if (arrayName.equals(ArrayHandler.USR_array.get(i).get(0))) {
				arrayNum = i;
			}
		}
		
		File dir = new File(fileName);
		if (!dir.exists()) {if (!dir.createNewFile()) {ChatHandler.warn(ChatHandler.color("red","Unable to create file!"));}}
		
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		
		if (arrayNum==-1) {
			writer.println("{");
			writer.println("}");
			returnString.append("{}");
		} else {
			writer.println("{");
			returnString.append("{");
			for (int i=1; i<ArrayHandler.USR_array.get(arrayNum).size(); i++) {
				String hasComma = "";
				if (i!=ArrayHandler.USR_array.get(arrayNum).size()-1) {hasComma = ",";}
				nodeName = nodeName.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
						.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
						.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				String nodeValue = ArrayHandler.USR_array.get(arrayNum).get(i).replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
						.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
						.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				writer.println("     \""+nodeName+"\":\""+nodeValue+"\""+hasComma);
				returnString.append("\"").append(nodeName).append("\":\"").append(nodeValue).append("\"").append(hasComma);
			}
			writer.println("}");
			returnString.append("}");
		}
		writer.close();
		
		return returnString.toString();
	}
	
	public static String importJsonFile(String type, String fileName, String toImport) {
		StringBuilder returnString = new StringBuilder("Something went wrong!");
		try {
			List<String> lines = new ArrayList<String>();
			String line;
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			
			StringBuilder jsonStringBuilder = new StringBuilder();
			for (String value : lines) {jsonStringBuilder.append(value);}
			String jsonString = jsonStringBuilder.toString().replace("[", "openSquareF6cyUQp9openSquare").replace("]", "closeSquareF6cyUQp9closeSquare")
					.replace("+", "plusF6cyUQp9plus").replace("-", "minusF6cyUQp9minus").replace("*", "timesF6cyUQp9times");
			
			if (toImport.contains("=>")) {
				if (type.equalsIgnoreCase("ARRAY")) {
					String arrayToSave = toImport.substring(0,toImport.indexOf("=>"));
					
					int whatArray = -1;
					for (int i=0; i<ArrayHandler.USR_array.size(); i++) {
						if (arrayToSave.equals(ArrayHandler.USR_array.get(i).get(0))) {
							whatArray = i;
						}
					}
					
					if (whatArray == -1) {
						List<String> temporary = new ArrayList<String>();
						temporary.add(arrayToSave);
                        ArrayHandler.USR_array.add(temporary);
						whatArray = ArrayHandler.USR_array.size()-1;
					}
					
					String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
					
					String check = "\""+jsonGet+"\""+":\"";
					if (jsonString.contains(check)) {
						returnString = new StringBuilder("[");
						while (jsonString.contains(check)) {
							String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
                            ArrayHandler.USR_array.get(whatArray).add(jsonGot.replace("openSquareF6cyUQp9openSquare","[").replace("closeSquareF6cyUQp9closeSquare","]")
									.replace("plusF6cyUQp9plus", "+").replace("minusF6cyUQp9minus", "-").replace("timesF6cyUQp9times", "*"));
							jsonString = jsonString.replaceFirst(check+jsonGot+"\"", "");
							returnString.append(jsonGot).append(",");
						}
						returnString = new StringBuilder(returnString.substring(0,returnString.length()-1)+"]");
					} else {
						returnString = new StringBuilder("No "+jsonGet+" in json!");
					}
				} else if (type.equalsIgnoreCase("STRING")) {
					String stringToSave = toImport.substring(0,toImport.indexOf("=>"));
					
					for (int i=0; i<global.USR_string.size(); i++) {
						if (stringToSave.equals(global.USR_string.get(i).get(0))) {
							String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
							
							String check = "\""+jsonGet+"\":\"";
							if (jsonString.contains(check)) {
								String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
								global.USR_string.get(i).set(1, jsonGot);
								returnString = new StringBuilder(jsonGot);
							}
						}
					}
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (stringToSave.equals(global.TMP_string.get(i).get(0))) {
							String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
							
							String check = "\""+jsonGet+"\":\"";
							if (jsonString.contains(check)) {
								String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
								global.TMP_string.get(i).set(1, jsonGot);
								returnString = new StringBuilder(jsonGot);
							}
						}
					}
				}
			} else {
				returnString = new StringBuilder("No array! use 'array=>nodes'");
			}
		} catch (UnsupportedEncodingException e) {
			returnString = new StringBuilder("Unsupported encoding!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			returnString = new StringBuilder("File not found!");
			e.printStackTrace();
		} catch (IOException e) {
			returnString = new StringBuilder("IO exception!");
			e.printStackTrace();
		}
		return returnString.toString();
	}
	
	public static String importJsonURL(String type, String url, String toImport) {
		StringBuilder returnString = new StringBuilder("Something went wrong!");
        StringBuilder jsonStringBuilder = new StringBuilder();

		if (jsonURL.containsKey(url)) {
            jsonStringBuilder = new StringBuilder(jsonURL.get(url));
        } else {
            try {
                URL web = new URL(url);
                InputStream fis = web.openStream();
                List<String> lines = new ArrayList<String>();
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
                bufferedReader.close();


                for (String value : lines) {jsonStringBuilder.append(value);}
                jsonStringBuilder = new StringBuilder(jsonStringBuilder.toString().replace("[", "openSquareF6cyUQp9openSquare").replace("]", "closeSquareF6cyUQp9closeSquare")
                        .replace("+", "plusF6cyUQp9plus").replace("-", "minusF6cyUQp9minus"));
                jsonURL.put(url, jsonStringBuilder.toString());

            } catch (UnsupportedEncodingException e) {
                returnString = new StringBuilder("Unsupported encoding!");
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                returnString = new StringBuilder("File not found!");
                e.printStackTrace();
            } catch (IOException e) {
                returnString = new StringBuilder("IO exception!");
                e.printStackTrace();
            }
        }

        String jsonString = jsonStringBuilder.toString();
        if (toImport.contains("=>")) {
            if (type.equalsIgnoreCase("ARRAY")) {
                String arrayToSave = toImport.substring(0,toImport.indexOf("=>"));

                int whatArray = -1;
                for (int i=0; i<ArrayHandler.USR_array.size(); i++) {
                    if (arrayToSave.equals(ArrayHandler.USR_array.get(i).get(0))) {
                        whatArray = i;
                    }
                }

                if (whatArray == -1) {
                    List<String> temporary = new ArrayList<String>();
                    temporary.add(arrayToSave);
                    ArrayHandler.USR_array.add(temporary);
                    whatArray = ArrayHandler.USR_array.size()-1;
                }

                String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());

                String check = "\""+jsonGet+"\":\"";

                if (jsonString.contains(check)) {
                    returnString = new StringBuilder("[");
                    while (jsonString.contains(check)) {
                        String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
                        ArrayHandler.USR_array.get(whatArray).add(jsonGot.replace("openSquareF6cyUQp9openSquare","[").replace("closeSquareF6cyUQp9closeSquare","]")
                                .replace("plusF6cyUQp9plus", "+").replace("minusF6cyUQp9minus", "-"));
                        jsonString = jsonString.replaceFirst(check+jsonGot+"\"", "");
                        returnString.append(jsonGot).append(",");
                    }
                    returnString = new StringBuilder(returnString.substring(0, returnString.length()-1) + "]");
                } else {
                    returnString = new StringBuilder("No "+jsonGet+" in json!");
                }
            } else if (type.equalsIgnoreCase("STRING")) {
                String stringToSave = toImport.substring(0,toImport.indexOf("=>"));

                for (int i=0; i<global.USR_string.size(); i++) {
                    if (stringToSave.equals(global.USR_string.get(i).get(0))) {
                        String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());

                        String check = "\""+jsonGet+"\":\"";
                        if (jsonString.contains(check)) {
                            String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
                            global.USR_string.get(i).set(1, jsonGot);
                            returnString = new StringBuilder(jsonGot);
                        }
                    }
                }
                for (int i=0; i<global.TMP_string.size(); i++) {
                    if (stringToSave.equals(global.TMP_string.get(i).get(0))) {
                        String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());

                        String check = "\""+jsonGet+"\":\"";
                        if (jsonString.contains(check)) {
                            String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
                            global.TMP_string.get(i).set(1, jsonGot);
                            returnString = new StringBuilder(jsonGot);
                        }
                    }
                }
            }
        } else {
            returnString = new StringBuilder("No array! use 'array=>nodes'");
        }

		return returnString.toString();
	}
}
