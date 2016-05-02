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
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

public class JsonHandler {
	public static String exportJsonFile(String fileName, String arrayName, String nodeName) throws IOException {
		String returnString;
		int arrayNum = -1;
		
		for (int i=0; i<global.USR_array.size(); i++) {
			if (arrayName.equals(global.USR_array.get(i).get(0))) {
				arrayNum = i;
			}
		}
		
		File dir = new File(fileName);
		if (!dir.exists()) {if (!dir.createNewFile()) {ChatHandler.warn(ChatHandler.color("red","Unable to create file!"));}}
		
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		
		if (arrayNum==-1) {
			writer.println("{");
			writer.println("}");
			returnString = "{}";
		} else {
			writer.println("{");
			returnString = ("{");
			for (int i=1; i<global.USR_array.get(arrayNum).size(); i++) {
				String hasComma = "";
				if (i!=global.USR_array.get(arrayNum).size()-1) {hasComma = ",";}
				nodeName = nodeName.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
						.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
						.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				String nodeValue = global.USR_array.get(arrayNum).get(i).replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
						.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
						.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				writer.println("     \""+nodeName+"\":\""+nodeValue+"\""+hasComma);
				returnString += ("\""+nodeName+"\":\""+nodeValue+"\""+hasComma);
			}
			writer.println("}");
			returnString += ("}");
		}
		writer.close();
		
		return returnString;
	}
	
	public static String importJsonFile(String type, String fileName, String toImport) {
		String returnString = "Something went wrong!";
		try {
			List<String> lines = new ArrayList<String>();
			String line;
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			
			String jsonString = "";
			for (String value : lines) {jsonString += value;}
			jsonString = jsonString.replace("[", "openSquareF6cyUQp9openSquare").replace("]", "closeSquareF6cyUQp9closeSquare")
					.replace("+", "plusF6cyUQp9plus").replace("-", "minusF6cyUQp9minus");
			
			if (toImport.contains("=>")) {
				if (type.equalsIgnoreCase("ARRAY")) {
					String arrayToSave = toImport.substring(0,toImport.indexOf("=>"));
					
					int whatArray = -1;
					for (int i=0; i<global.USR_array.size(); i++) {
						if (arrayToSave.equals(global.USR_array.get(i).get(0))) {
							whatArray = i;
						}
					}
					
					if (whatArray == -1) {
						List<String> temporary = new ArrayList<String>();
						temporary.add(arrayToSave);
						global.USR_array.add(temporary);
						whatArray = global.USR_array.size()-1;
					}
					
					String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
					
					String check = "\""+jsonGet+"\""+":\"";
					if (jsonString.contains(check)) {
						returnString = "[";
						while (jsonString.contains(check)) {
							String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
							global.USR_array.get(whatArray).add(jsonGot.replace("openSquareF6cyUQp9openSquare","[").replace("closeSquareF6cyUQp9closeSquare","]")
									.replace("plusF6cyUQp9plus", "+").replace("minusF6cyUQp9minus", "-"));
							jsonString = jsonString.replaceFirst(check+jsonGot+"\"", "");
							returnString += jsonGot+",";
						}
						returnString = returnString.substring(0,returnString.length()-1)+"]";
					} else {
						returnString = "No "+jsonGet+" in json!";
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
								returnString = jsonGot;
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
								returnString = jsonGot;
							}
						}
					}
				}
			} else {
				returnString = "No array! use 'array=>nodes'";
			}
		} catch (UnsupportedEncodingException e) {
			returnString = "Unsupported encoding!";
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			returnString = "File not found!";
			e.printStackTrace();
		} catch (IOException e) {
			returnString = "IO exception!";
			e.printStackTrace();
		}
		return returnString;
	}
	
	public static String importJsonURL(String type, String url, String toImport) {
		String returnString = "Something went wrong!";
        String jsonString = "";

		if (global.jsonURL.containsKey(url)) {
            jsonString = global.jsonURL.get(url);
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


                for (String value : lines) {jsonString += value;}
                jsonString = jsonString.replace("[", "openSquareF6cyUQp9openSquare").replace("]", "closeSquareF6cyUQp9closeSquare")
                        .replace("+", "plusF6cyUQp9plus").replace("-", "minusF6cyUQp9minus");
                global.jsonURL.put(url, jsonString);

            } catch (UnsupportedEncodingException e) {
                returnString = "Unsupported encoding!";
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                returnString = "File not found!";
                e.printStackTrace();
            } catch (IOException e) {
                returnString = "IO exception!";
                e.printStackTrace();
            }
        }

        if (toImport.contains("=>")) {
            if (type.equalsIgnoreCase("ARRAY")) {
                String arrayToSave = toImport.substring(0,toImport.indexOf("=>"));

                int whatArray = -1;
                for (int i=0; i<global.USR_array.size(); i++) {
                    if (arrayToSave.equals(global.USR_array.get(i).get(0))) {
                        whatArray = i;
                    }
                }

                if (whatArray == -1) {
                    List<String> temporary = new ArrayList<String>();
                    temporary.add(arrayToSave);
                    global.USR_array.add(temporary);
                    whatArray = global.USR_array.size()-1;
                }

                String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());

                String check = "\""+jsonGet+"\":\"";
                if (jsonString.contains(check)) {
                    returnString = "[";
                    while (jsonString.contains(check)) {
                        String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
                        global.USR_array.get(whatArray).add(jsonGot.replace("openSquareF6cyUQp9openSquare","[").replace("closeSquareF6cyUQp9closeSquare","]")
                                .replace("plusF6cyUQp9plus", "+").replace("minusF6cyUQp9minus", "-"));
                        jsonString = jsonString.replaceFirst(check+jsonGot+"\"", "");
                        returnString += jsonGot+",";
                    }
                    returnString = returnString.substring(0, returnString.length()-1) + "]";
                } else {
                    returnString = "No "+jsonGet+" in json!";
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
                            returnString = jsonGot;
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
                            returnString = jsonGot;
                        }
                    }
                }
            }
        } else {
            returnString = "No array! use 'array=>nodes'";
        }

		return returnString;
	}
}
