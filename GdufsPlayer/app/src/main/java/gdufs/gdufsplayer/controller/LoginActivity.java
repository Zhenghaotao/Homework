package gdufs.gdufsplayer.controller;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gdufs.gdufsplayer.R;

/**
 * Created by taotao on 16-3-2.
 */
public class LoginActivity extends Activity {


    private Button btn_login;
    private EditText et_account;
    private EditText et_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initEvent() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.toString().trim();
                String pass = et_pass.toString().trim();
                if(TextUtils.isEmpty(account)){
                    Toast.makeText(LoginActivity.this,
                            R.string.account_is_null,Toast.LENGTH_SHORT).show();

                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this,
                            R.string.password_is_null,Toast.LENGTH_SHORT).show();
                    return;

                }

            }
        });

    }

    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        et_account = (EditText) findViewById(R.id.et_account);
        et_pass = (EditText) findViewById(R.id.et_password);

    }

}
