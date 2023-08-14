package tests;

import com.google.gson.Gson;

public class GsontoJSON {
    public static String
    convettoJSN(Object obj){
        return (new
                Gson().toJson(obj));
    }
}
