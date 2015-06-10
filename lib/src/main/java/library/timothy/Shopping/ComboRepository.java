package library.timothy.Shopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComboRepository {

    private static final Map<String, Combo> combos = new HashMap<String, Combo>();

    public static void refreshData(JSONArray jsonArray) {
        combos.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Combo comboVo = new Combo();
                String comboId = jsonObject.getString("Id");
                comboVo.setId(comboId);

                JSONArray detailsArray = jsonObject.getJSONArray("Detail");

                for (int j = 0; j < detailsArray.length(); j++) {
                    JSONObject detailJsonObject = detailsArray.getJSONObject(j);

                    ComboDetail detailVo = new ComboDetail();

                    detailVo.setProductId(detailJsonObject.getString("Id"));
                    detailVo.setQuantity(detailJsonObject.getInt("Quantity"));

                    comboVo.getDetails().add(detailVo);
                }

                combos.put(comboId, comboVo);
            }


        } catch (JSONException e) {
        }
    }


    public static Map<String, Combo> getCombosMap() {
        return combos;
    }

    public static List<Combo> getCombos() {
        List<Combo> list = new LinkedList<Combo>();
        list.addAll(combos.values());
        return list;
    }
}
