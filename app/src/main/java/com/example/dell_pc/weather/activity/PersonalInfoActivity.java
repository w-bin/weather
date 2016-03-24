package com.example.dell_pc.weather.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.util.ImgUtil;
import com.example.dell_pc.weather.util.LogUtil;

/**
 * Created by dell-pc on 2016/3/21.
 */
public class PersonalInfoActivity extends Activity implements View.OnClickListener {

    public static final int CHOOSE_PHOTO = 1;
    private Button cancelBtn;
    private Button submitBtn;
    private Button alterBtn;
    private EditText nameText;
    private EditText introductionText;
    private ImageView headImg;
    private String name, introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_layout);
        init();
    }

    private void init() {
        cancelBtn = (Button) findViewById(R.id.personal_info_cancel_button);
        submitBtn = (Button) findViewById(R.id.personal_info_submit_button);
        alterBtn = (Button) findViewById(R.id.personal_info_alterImg_button);
        nameText = (EditText) findViewById(R.id.personal_info_name);
        introductionText = (EditText) findViewById(R.id.personal_info_introduction);
        headImg = (ImageView) findViewById(R.id.personal_info_header_img);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nameText.setText(sharedPreferences.getString("personal_name", ""));
        introductionText.setText(sharedPreferences.getString("personal_introduction", ""));
        headImg.setImageBitmap(ImgUtil.getImage(sharedPreferences.getString("personal_img", "")));

        cancelBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        alterBtn.setOnClickListener(this);
        nameText.setOnClickListener(this);
        introductionText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.personal_info_cancel_button:

                finish();
                break;
            case R.id.personal_info_submit_button:
                name = nameText.getText().toString();
                introduction = introductionText.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(introduction)) {
                    new AlertDialog.Builder(this).setTitle("请注意")
                            .setMessage("姓名和描述不能为空。")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                } else {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                    editor.putString("personal_name", name);
                    editor.putString("personal_introduction", introduction);
                    editor.putString("personal_img",
                            PreferenceManager.getDefaultSharedPreferences(this).getString("personal_tmp_img", ""));
                    editor.commit();
                    Intent intent = new Intent(PersonalInfoActivity.this, MyBaseActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.personal_info_alterImg_button:
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e(PersonalInfoActivity.class + "", "hello");
        switch (requestCode) {
            case CHOOSE_PHOTO:
                LogUtil.e(PersonalInfoActivity.class + "", "2333");
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    LogUtil.e(PersonalInfoActivity.class + "", "okok");
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageOnKitKat(this, data);
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                    }
                    ImageView view = (ImageView) findViewById(R.id.personal_info_header_img);
                    view.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }
}
