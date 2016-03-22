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
/*
    //4.4及以上系统使用这个方法处理图片
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];  //解析出数字格式的id
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果不是document类型的Uri,则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }
        displayImage(imagePath);
    }

    //4.4以下系统使用这个方法处理图片
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri, null, selection, null, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
            //Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            Bitmap bitmap=getImage(imagePath);
            ImageView view=(ImageView)findViewById(R.id.personal_info_header_img);
            view.setImageBitmap(bitmap);
            Toast.makeText(this,"success"+imagePath,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }*/
}
