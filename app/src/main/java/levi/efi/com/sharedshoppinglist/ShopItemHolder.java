package levi.efi.com.sharedshoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by P0020077 on 10/07/2017.
 */

public class ShopItemHolder  extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {

    EditText txtItemDesc;
    CheckBox cbIsBought;
    private int originalPaintFlags;
    Saveable savable;

    public ShopItemHolder(View itemView, final Context context, Saveable savableObject) {
        super(itemView);
        savable = savableObject;
        txtItemDesc = (EditText) itemView.findViewById(R.id.txtItemDesc);
        cbIsBought = (CheckBox) itemView.findViewById(R.id.cbIsBought);

        originalPaintFlags = txtItemDesc.getPaintFlags();

        cbIsBought.setOnCheckedChangeListener(this);
        txtItemDesc.setOnEditorActionListener(this);
        txtItemDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.getId() == R.id.txtItemDesc){

                    if (!b) {
                        if (view.getTag() != null) {
                            ((ShopItem)view.getTag()).setProduct(txtItemDesc.getText().toString().trim());
                            ((ShopItem)view.getTag()).setBought(cbIsBought.isChecked());
                        }

                        Toast.makeText(context,"needtosave",Toast.LENGTH_SHORT).show();
                        savable.SaveData();
                    }
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.cbIsBought){

            if (!(txtItemDesc.getText().toString().trim().equals("") && cbIsBought.isChecked())) {
                setBoughtFlags();

                if (compoundButton.getTag() != null) {
                    ((ShopItem) compoundButton.getTag()).setProduct(txtItemDesc.getText().toString().trim());
                    ((ShopItem) compoundButton.getTag()).setBought(cbIsBought.isChecked());
                }

                savable.SaveData();
            }
        }


    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean retValue = false;

        switch (textView.getId()) {
            case R.id.txtItemDesc: {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    retValue = true;
                    savable.goToNextItem(((ShopItem)textView.getTag()).getPosition());
                }
                break;
            }

            default:
                break;
        }

        return retValue;
    }

    public void setBoughtFlags() {
        if (cbIsBought.isChecked()) {
            txtItemDesc.setPaintFlags(originalPaintFlags | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            txtItemDesc.setPaintFlags(originalPaintFlags);
        }
    }
}
