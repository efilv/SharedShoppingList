package levi.efi.com.sharedshoppinglist;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class ShopItemActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private EditText txtItemDesc;
    private CheckBox cbIsBought;
    private int originalPaintFlags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);

        txtItemDesc = (EditText) findViewById(R.id.txtItemDesc);
        cbIsBought = (CheckBox) findViewById(R.id.cbIsBought);

        originalPaintFlags = txtItemDesc.getPaintFlags();

        cbIsBought.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.cbIsBought){
            if (cbIsBought.isChecked()){
                txtItemDesc.setPaintFlags(originalPaintFlags | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                txtItemDesc.setPaintFlags(originalPaintFlags);
            }
        }

    }
}
