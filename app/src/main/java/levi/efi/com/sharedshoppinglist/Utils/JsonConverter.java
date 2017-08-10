package levi.efi.com.sharedshoppinglist.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import levi.efi.com.sharedshoppinglist.ShopItem;

/**
 * Created by P0020077 on 16/07/2017.
 */

public class JsonConverter {

    public static ArrayList<ShopItem> GetShopList(String jsonString){

        ArrayList<ShopItem> lstShopList = new ArrayList<ShopItem>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray itemsArr = jsonObject.getJSONArray("items");

            for (int i=0; i< itemsArr.length(); i++){
                ShopItem itm = ShopItem.getShopItemFromJson(itemsArr.getJSONObject(i));

                if (itm != null){
                    lstShopList.add(itm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstShopList;
    }
}
