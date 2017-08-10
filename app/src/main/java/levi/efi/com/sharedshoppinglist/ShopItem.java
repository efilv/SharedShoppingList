package levi.efi.com.sharedshoppinglist;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by P0020077 on 10/07/2017.
 */

public class ShopItem {
    public final static String PRODUCT = "product";
    public final static String IS_BOUGHT = "isBought";

    private String product;
    private boolean isBought;
    private int position;

    public ShopItem(String product, boolean isBought) {
        this.product = product;
        this.isBought = isBought;
    }

    public static ShopItem getShopItemFromJson(JSONObject jsonObject) {
        ShopItem item = null;

        try {
            item = new ShopItem(jsonObject.getString(PRODUCT), jsonObject.getBoolean(IS_BOUGHT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return item;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public String getProduct() {
        return product;
    }

    public boolean isBought() {
        return isBought;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "{\"" + PRODUCT + "\":\"" + this.getProduct() + "\",\"" + IS_BOUGHT + "\":\"" + this.isBought() + "\"}";
    }

    @Override
    public boolean equals(Object obj) {
        boolean retValue = super.equals(obj);

        if (obj.getClass() == this.getClass()){
            ShopItem shopItem = (ShopItem)obj;
            retValue = this.isBought == shopItem.isBought && this.product.equals(shopItem.product);
        }

        return retValue;
    }
}
