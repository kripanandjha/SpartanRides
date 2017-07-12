package com.android.spartanrides;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by venka on 7/1/2017.
 */

public class JSONActivity {

        public static int JSON_PRINT_INDENT_FACTOR = 4;

        public static void printJSON(JSONObject jsonObject) {
            try {
                String jsonPrettyPrintString = jsonObject.toString(JSON_PRINT_INDENT_FACTOR);
                System.out.println(jsonPrettyPrintString);
            } catch (JSONException je) {
                System.out.println(je.toString());
            }
        }

    public void JSONTransmitter(JSONObject jsonObject) {
        ServerTask task = new ServerTask();
        task.jsonObject = jsonObject;
        task.execute();
    }
}

