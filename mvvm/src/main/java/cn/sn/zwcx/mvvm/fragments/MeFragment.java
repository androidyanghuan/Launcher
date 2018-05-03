package cn.sn.zwcx.mvvm.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.activitys.AboutActivity;
import cn.sn.zwcx.mvvm.constants.Constant;
import cn.sn.zwcx.mvvm.databinding.FragmentMeBinding;
import cn.sn.zwcx.mvvm.databinding.MePopupWindowBinding;
import cn.sn.zwcx.mvvm.utils.MyFileProvider;
import cn.sn.zwcx.mvvm.utils.SPUtils;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import cn.sn.zwcx.mvvm.views.CustomPopupWindow;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/4/19 11:06.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * https://blog.csdn.net/lmj623565791/article/details/72859156
 * 小米5s7.0系统从相册选取图片
 */

public class MeFragment extends Fragment implements View.OnClickListener {
    private final String TAG = MeFragment.class.getSimpleName();

    private FragmentMeBinding mBinding;

    private CustomPopupWindow mWindow;

    private MePopupWindowBinding mPopBinding;

    private PopupWindow mPopupWindow;

    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_ALBUM = 200;
    public static final int CROP_PICTURE = 300;

    private Uri cropImageUri = null;
    private File photoFile = null;

    public static MeFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString("title",title);
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me,container,false);
        initViews();
        return mBinding.getRoot();
    }

    private void initViews() {
        String title = getArguments().getString("title");
        mBinding.setTitle(title);
        String photo = SPUtils.getInstance(getActivity()).getUserPhoto();
        if (TextUtils.isEmpty(photo))
            mBinding.setIcon(Constant.MAIN_NV_USER_ICON);
        else
            mBinding.setIcon(photo);
        mBinding.executePendingBindings();
        mBinding.fragmentMeSetting.setOnClickListener(this);
        mBinding.fragmentMeIcon.setOnClickListener(this);
        mBinding.fragmentMeAbout.setOnClickListener(this);
    }

    private void initPopupView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        mWindow = new CustomPopupWindow(getActivity(),R.layout.me_popup_window,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                mPopBinding = MePopupWindowBinding.bind(getContentView());
            }

            @Override
            protected void initEvent() {
                mPopBinding.mePopupWindowPhotograph.setOnClickListener(MeFragment.this);
                mPopBinding.mePopupWindowAlbum.setOnClickListener(MeFragment.this);
                mPopBinding.mePopupWindowCancel.setOnClickListener(MeFragment.this);

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                mPopupWindow = getPopupWindow();
                mPopupWindow.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getActivity().getWindow().setAttributes(lp);
                    }
                });
            }
        };
        mWindow.showAtLocation(mBinding.fragmentMeParent,Gravity.BOTTOM,0,0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_me_setting:
                Intent settingIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(settingIntent);
                break;
            case R.id.fragment_me_icon:
                initPopupView();
                break;
            case R.id.me_popup_window_photograph:
                checkedPermission(Manifest.permission.CAMERA,REQUEST_CAMERA);
                mPopupWindow.dismiss();
                break;
            case R.id.me_popup_window_album:
                checkedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_ALBUM);
                mPopupWindow.dismiss();
                break;
            case R.id.me_popup_window_cancel:
                mPopupWindow.dismiss();
                break;
            case R.id.fragment_me_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
        }
    }

    private void checkedPermission(String permission, final int method) {
        RxPermissions permissions = new RxPermissions(getActivity());
        permissions.request(permission)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            switch (method) {
                                case REQUEST_CAMERA:
                                    openCamera();
                                    break;
                                case REQUEST_ALBUM:
                                    openAlbum();
                                    break;
                            }
                        else
                            ToastUtil.showToast(R.string.must_permission);
                    }
                });
    }

    private void openCamera() {

        String fileName = new SimpleDateFormat("yyyyMMddHHmmss",Locale.CHINA).format(new Date()) + ".png";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            photoFile = new File(Environment.getExternalStorageDirectory() + "/Information/UserPhoto",fileName);
        else
            photoFile = new File(getActivity().getExternalCacheDir(),fileName);
        if (!photoFile.getParentFile().exists())
            photoFile.mkdirs();
        try {
            photoFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showToast(R.string.save_user_photo_faild);
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = MyFileProvider.getUriForFile(getActivity(),photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        else
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.please_select_album)),REQUEST_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK)
                    startPhotoZoom(MyFileProvider.getUriForFile(getActivity(),photoFile));
                break;
            case REQUEST_ALBUM:
                if (resultCode == Activity.RESULT_OK && data != null)
                        handlerImageBeforeKitkat(data);
                    break;

            case CROP_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    mBinding.fragmentMeIcon.setImageURI(cropImageUri);
                    SPUtils.getInstance(getActivity()).saveUserPhoto(cropImageUri.toString());
                    if (mListener != null)
                        mListener.onUserPhotoChange();
                }
                break;

        }
    }

    private void handlerImageBeforeKitkat(Intent data) {
        Uri cropUri = data.getData();
        startPhotoZoom(cropUri);
    }

    private void handlerImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.e(TAG,"uri:" + uri.toString());
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
                Log.e(TAG,"image path:" + imagePath);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                imagePath = getImagePath(contentUri,null);
                Log.e(TAG,"content uri to image path:" + imagePath);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri,null);
            Log.e(TAG,"content type is uri to image path:" + imagePath);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
            Log.e(TAG,"file type is uri to image path:" + imagePath);
        }
        startPhotoZoom(uri);
    }

    private void startPhotoZoom(Uri uri) {
        File cropFile = null;
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date()) + ".png";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cropFile = new File(Environment.getExternalStorageDirectory() + "/Information/UserPhoto",fileName);
        else
            cropFile = new File(getActivity().getExternalCacheDir(),fileName);
        if (!cropFile.getParentFile().exists())
            cropFile.mkdirs();
        try {
            cropFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showToast(R.string.save_user_photo_faild);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropImageUri = MyFileProvider.getUriForFile(getActivity(),cropFile);
        intent.setDataAndType(uri,"image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", false);
        cropImageUri = Uri.parse("file:///" + cropFile.getAbsolutePath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CROP_PICTURE);

    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = "";
        Cursor cursor = getActivity().getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null)
            if (cursor.moveToFirst())
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public interface OnUserPhotoChangeListener{
        void onUserPhotoChange();
    }

    private OnUserPhotoChangeListener mListener;

    public void setOnUserPhotoChangeListener(OnUserPhotoChangeListener listener) {
        this.mListener = listener;
    }

}
