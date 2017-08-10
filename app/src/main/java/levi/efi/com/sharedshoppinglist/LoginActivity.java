package levi.efi.com.sharedshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText txtUserName, txtPassword;
    private Button btnLogin;
    private ProgressBar pbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.loginLayout));

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        pbProgress = (ProgressBar) findViewById(R.id.loginProgressBar);

        btnLogin.setOnClickListener(this);
        txtPassword.setOnEditorActionListener(this);

        //Get country code
        String code = getCountryDialCode();

        loadCredentials();
    }

    public String getCountryDialCode() {
        String contryId = null;
        String contryDialCode = null;

        TelephonyManager telephonyMngr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode = this.getResources().getStringArray(R.array.DialingCountryCode);
        for (int i = 0; i < arrContryCode.length; i++) {
            String[] arrDial = arrContryCode[i].split(",");
            if (arrDial[1].trim().equals(contryId.trim())) {
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;
    }

    private void login() {
        //check credentials
        pbProgress.setVisibility(View.VISIBLE);

        initDatabase();
    }

    private void initDatabase() {
        final String userName = txtUserName.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userName);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean valid = false;

                if (dataSnapshot.getValue() != null && dataSnapshot.hasChild("password")) {
                    Object dbPassword = dataSnapshot.child("password").getValue();

                    if (password.equals(dbPassword.toString())) {
                        valid = true;

                        SaveCredentials();

                        Intent intent = new Intent(LoginActivity.this, ShopListActivity.class);
                        intent.putExtra("username", userName);
                        startActivity(intent);
                    }
                }

                if (!valid) {
                    Toast.makeText(LoginActivity.this, "Worng user name or password", Toast.LENGTH_LONG).show();
                }

                pbProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("database", databaseError.getMessage());
            }
        });

//        ArrayList<ShopItem> arr = new ArrayList<>();
//        ShopItem s = new ShopItem("בננה", false);
//        ShopItem s1 = new ShopItem("תפוח", false);
//        ShopItem s2 = new ShopItem("מלון", false);
//        arr.add(s);
//        arr.add(s1);
//        arr.add(s2);
//        String aaa = myRef.getKey();
//        myRef.setValue(arr);
    }

    private void SaveCredentials() {
        SharedPreferences sharedPref = this.getPreferences(this.getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.userName), txtUserName.getText().toString().trim());
        editor.putString(getString(R.string.password), txtPassword.getText().toString().trim());
        editor.commit();

    }

    private void loadCredentials() {
        SharedPreferences sharedPref = this.getPreferences(this.getApplicationContext().MODE_PRIVATE);
        txtUserName.setText(sharedPref.getString(getString(R.string.userName), ""));
        txtPassword.setText(sharedPref.getString(getString(R.string.password), ""));

        if (!txtUserName.getText().equals("") && !txtPassword.getText().equals("")){
            login();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin: {
                login();
                break;
            }

            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean retValue = false;

        switch (textView.getId()) {
            case R.id.txtPassword: {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    retValue = true;
                    login();
                }
                break;
            }

            default:
                break;
        }

        return retValue;
    }
}
