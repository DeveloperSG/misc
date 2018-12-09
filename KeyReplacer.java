package kz.sgsoft;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class KeyReplacer {

    public static void main(String[] args) throws IOException {
        String jsonString = readFile("json.json");
        if (jsonString != null && jsonString.length() > 1) {
            JSONObject jsonObject = new JSONObject(jsonString);
            iterateJSON(jsonObject);
            writeFile(jsonObject.toString(2));
        }
    }

    public static void  iterateJSON(JSONObject obj) {
        String[] keys = obj.keySet().toArray(new String[]{});
        for (int i = 0; i < keys.length; i++) {
            Object nestedObject = obj.get(keys[i]);
            System.out.println("key = " + keys[i]);
            if (keys[i].contains(".")) {
                String oldKey = keys[i];
                System.out.println("Replacing dot in key = " + keys[i]);
                String newKey = keys[i].replace(".", "_");
                obj.remove(oldKey);
                obj.put(newKey, nestedObject);
            }
            if (nestedObject instanceof JSONObject) {
                iterateJSON((JSONObject)nestedObject);
            } else if (nestedObject instanceof JSONArray) {
                JSONArray array = (JSONArray) nestedObject;
                for (int j = 0; j < array.length(); j++) {
                    Object arrayObject = array.get(j);
                    if (arrayObject instanceof JSONObject) {
                        iterateJSON((JSONObject)arrayObject);
                    }
                }
            }
        }
    }

    private static String readFile(String fileName) {
        File file = new File(fileName);
        StringBuilder sb = new StringBuilder();
        FileReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new FileReader(fileName);
            bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
        /*StringBuilder sb = new StringBuilder();
        InputStream is = null;
        Reader reader = null;
        try {
            is = new FileInputStream("json.json");
            reader = new InputStreamReader(is);
            int c = 0;
            while ((c = reader.read()) != -1) {
                sb.append((char)c);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();*/
    }

    private static void writeFile(String string) throws IOException {
        File file = new File("json_modified.json");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();;
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
