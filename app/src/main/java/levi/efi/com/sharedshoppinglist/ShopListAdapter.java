package levi.efi.com.sharedshoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;

public class ShopListAdapter extends RecyclerView.Adapter<ShopItemHolder> implements ItemTouchHelperAdapter{

    private final ArrayList<ShopItem> data;
    private final LayoutInflater inflater;
    private final Context context;
    private int originalPaintFlags;
    private Saveable saveable;

    public ShopListAdapter(ArrayList<ShopItem> data, LayoutInflater inflater, Context context, Saveable savableObject) {
        this.data = data;
        this.inflater = inflater;
        this.context = context;
        this.saveable = savableObject;

        originalPaintFlags = new EditText(context).getPaintFlags();
    }

    @Override
    public ShopItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.activity_shop_item, parent, false);
        return new ShopItemHolder(convertView, context, saveable);
    }

    @Override
    public void onBindViewHolder(ShopItemHolder holder, int position) {
        ShopItem item = data.get(position);
        item.setPosition(position);

        holder.cbIsBought.setChecked(item.isBought());
        holder.txtItemDesc.setText(item.getProduct());
        holder.txtItemDesc.setTag(item);
        holder.cbIsBought.setTag(item);

        holder.setBoughtFlags();

        if (saveable.getAddState() && position == getItemCount() - 1){
            holder.txtItemDesc.requestFocus();
            saveable.setAddState(false);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<ShopItem> getData() {
        return data;
    }

    public void setData(ArrayList<ShopItem> tmpData) {
        data.clear();
        data.addAll(tmpData);
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        saveable.SaveData();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        saveable.SaveData();
    }

    @Override
    public void setFocus(RecyclerView.ViewHolder viewHolder) {
        Object b = viewHolder.getClass();
        Log.i("efi",viewHolder.getClass().toString());

        ((ShopItemHolder)viewHolder).txtItemDesc.requestFocus();
    }
}