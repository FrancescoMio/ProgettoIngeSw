package Libreria;import org.json.simple.JSONObject;

public class LeggiJSON {
    JSONObject obj;
    public LeggiJSON(){
        obj = new JSONObject();
        obj.put("name","sonoo");
        obj.put("age",16);
        obj.put("salary",1200.50);
        System.out.println(obj);
    }
}
