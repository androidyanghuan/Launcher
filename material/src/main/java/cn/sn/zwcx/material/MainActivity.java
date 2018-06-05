package cn.sn.zwcx.material;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import cn.sn.zwcx.material.utils.RegularUtil;
import cn.sn.zwcx.material.widgets.CustomTextWatcher;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    private TextInputLayout mUserInput,mPhoneNumberInput,mPasswordInput;

    private TextInputEditText mPasswordEdit;

    private EditText mUserEdit,mPhoneEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_main);
        setContentView(R.layout.layout);
        initViews();
    }

    private void initViews() {
        mPasswordInput = findViewById(R.id.text_input_layout_password);
        mPhoneNumberInput = findViewById(R.id.text_input_layout_phone);
        mUserInput = findViewById(R.id.text_input_layout_user);
        // 设置可以计数
        mUserInput.setCounterEnabled(true);
        mPhoneNumberInput.setCounterEnabled(true);
        mPasswordInput.setCounterEnabled(true);
        // 设置计数最大值
        mUserInput.setCounterMaxLength(20);
        mPhoneNumberInput.setCounterMaxLength(11);
        mPasswordInput.setCounterMaxLength(6);

        mUserEdit = findViewById(R.id.text_input_user);
        mPhoneEdit = findViewById(R.id.text_input_phone);
        mPasswordEdit = findViewById(R.id.text_input_password);
        mUserEdit.addTextChangedListener(new CustomTextWatcher(mUserInput,mUserEdit,"用户名"));
        mPhoneEdit.addTextChangedListener(new CustomTextWatcher(mPhoneNumberInput,mPhoneEdit,"手机号码"));
        mPasswordEdit.addTextChangedListener(new CustomTextWatcher(mPasswordInput,mPasswordEdit,"密码"));

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
