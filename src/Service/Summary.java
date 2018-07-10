package Service;

import org.json.JSONObject;

public class Summary {

    private JSONObject summary;

    public Summary(){
        summary = new JSONObject();
    }

    public void addEle(String key, Object value){
        summary.put(key, value);
    }

    public String toString(){
        return summary.toString();
    }

}
