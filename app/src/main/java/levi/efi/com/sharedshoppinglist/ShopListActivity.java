package levi.efi.com.sharedshoppinglist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import levi.efi.com.sharedshoppinglist.Utils.JsonConverter;

public class ShopListActivity extends BaseActivity implements View.OnClickListener, RecyclerView.OnItemTouchListener, Saveable {

    private LinearLayoutManager linearLayoutManager;
    private TextView txtEmptyMessage;
    private Button btnAddItem, btnRemoverItem;
    private RecyclerView lstItemsView;
    private ArrayList<ShopItem> lstItemsModel;
    private ShopListAdapter adapter;
    private ProgressBar pbProgress;
    private boolean isAddState = false;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        setupUI(findViewById(R.id.shopListLayout));

        username = getIntent().getStringExtra("username");

        lstItemsModel = new ArrayList<ShopItem>();

        txtEmptyMessage = (TextView) findViewById(R.id.txtEmptyMessage);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        lstItemsView = (RecyclerView) findViewById(R.id.itemList);
        pbProgress = (ProgressBar) findViewById(R.id.shopListProgressBar);

        btnAddItem.setOnClickListener(this);

        adapter = new ShopListAdapter(lstItemsModel, getLayoutInflater(), ShopListActivity.this, ShopListActivity.this);
        lstItemsView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        lstItemsView.setLayoutManager(linearLayoutManager);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(lstItemsView);

        populateShopList();
        populateListView();
    }

    private void showEmptyIfEmpty() {
        if (adapter.getItemCount() == 0) {
            txtEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            txtEmptyMessage.setVisibility(View.INVISIBLE);
        }
    }

    private void populateListView() {
        if (!isIdentical(lstItemsModel, adapter.getData())) {
            adapter.setData(lstItemsModel);
            showEmptyIfEmpty();
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isIdentical(ArrayList<ShopItem> providedData, ArrayList<ShopItem> currentData) {
        boolean retValue = true;

        if (currentData.size() != providedData.size()) {
            retValue = false;
        } else {
            for (int i = 0; i < currentData.size(); i++) {
                if (!currentData.get(i).equals(providedData.get(i))) {
                    retValue = false;
                    break;
                }
            }
        }

        return retValue;
    }

    private void populateShopList() {
//        lstItemsModel.add(new ShopItem("בננות", false));
//        lstItemsModel.add(new ShopItem("עגבניות", false));
//        lstItemsModel.add(new ShopItem("בטטות", false));
//        lstItemsModel.add(new ShopItem("שוקולד", false));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(username);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int a = 0;

                if (dataSnapshot.getKey().equals("list")) {
                    lstItemsModel = getShopListFromHashMap((ArrayList<HashMap<String, Object>>) dataSnapshot.getValue());
                    populateListView();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int a = 0;

                if (dataSnapshot.getKey().equals("list")) {
                    lstItemsModel = getShopListFromHashMap((ArrayList<HashMap<String, Object>>) dataSnapshot.getValue());
                    populateListView();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //ListToJson();
        // Log.i("json",ListToJson());

        //JsonConverter.GetShopList(ListToJson());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAddItem) {
            addShopListItem();
        }
    }

    private void addShopListItem() {
        setAddState(true);
        adapter.getData().add(new ShopItem("", false));
        showEmptyIfEmpty();
        adapter.notifyDataSetChanged();
        lstItemsView.scrollToPosition(adapter.getItemCount() - 1);
    }

    public ArrayList<ShopItem> getShopListFromHashMap(ArrayList<HashMap<String, Object>> hashMap) {
        ArrayList<ShopItem> lst = new ArrayList<ShopItem>();

        for (HashMap<String, Object> item : hashMap) {
            try {
                String product = item.get("product").toString();
                String bought = item.get("bought").toString();
                boolean bool = Boolean.parseBoolean(bought);

                lst.add(new ShopItem(product, bool));
            } catch (Exception ex) {
                Log.e("error", ex.getMessage());
                Log.e("error", ex.getStackTrace().toString());
                Log.e("error", ex.getLocalizedMessage());
            }
        }

        return lst;
    }

    public String ListToJson() {
        String str = null;

        if (lstItemsModel.size() > 0) {
            str = "{\"items\" : [";

            for (ShopItem item : lstItemsModel) {
                str += item.toString() + ",";
            }

            str = str.substring(0, str.length() - 1);

            str += "]}";
        }

        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu, menu);
        //return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retValue = false;

//        if(item.getItemId() == R.id.save){
//            String str = ListToJson();
//
//            //Save data on the web
//            //1.check if ther is a merge we need to do;
//            //2.save
//
//            retValue = true;
//        }

        return retValue;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void SaveData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(username).child("list");

        lstItemsModel = adapter.getData();
//        HashMap<Integer, ShopItem> itemsToRemove = new HashMap<Integer, ShopItem>();
//
//        for (int i = 0; i < lstItemsModel.size(); i++) {
//            ShopItem item = lstItemsModel.get(i);
//
//            if (item.getProduct().trim().equals("")) {
//                itemsToRemove.put(i, item);
//                //lstItemsModel.remove(item);
//            }
//        }
//
//        for (int i = 0; i < itemsToRemove.size(); i++) {
//            lstItemsModel.remove(i);
//            adapter.notifyItemRemoved(i);
//            adapter.notifyItemRangeChanged(i, lstItemsModel.size());
//        }


        myRef.setValue(lstItemsModel);
    }

    @Override
    public boolean getAddState() {
        return isAddState;
    }

    @Override
    public void setAddState(boolean state) {
        isAddState = state;
    }

    @Override
    public void goToNextItem(int currPosition) {
        if (currPosition == adapter.getItemCount() - 1){
            addShopListItem();
        }
        else{
            View  item = linearLayoutManager.findViewByPosition(currPosition + 1);
            if (item != null){
                item.findViewById(R.id.txtItemDesc).requestFocus();
            }
        }
    }
}
