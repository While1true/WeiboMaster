package com.update;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;



/**
 * Created by vange on 2017/12/15.
 */

public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private View inflate;
    private MyCallback<MyDialogFragment> callback;
    UpdateBean bean;

    public MyDialogFragment setCallback(MyCallback<MyDialogFragment> callback) {
        this.callback = callback;
        return this;
    }

    private int layout;

    public MyDialogFragment setLayout(int layout) {
        this.layout = layout;
        return this;
    }

    public MyDialogFragment setBean(UpdateBean bean) {
        this.bean = bean;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(layout, container, false);
        initView();
        return inflate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void initView() {

        TextView info=inflate.findViewById(R.id.info);
        TextView version=inflate.findViewById(R.id.version);
        TextView size=inflate.findViewById(R.id.size);
        size.setText("文件大小：" + bean.getAppSize());
        version.setText("版本号：" + bean.getVersionNumber());
        info.setText(bean.getUpdateInformation());

        inflate.findViewById(R.id.cancel).setOnClickListener(this);
        inflate.findViewById(R.id.confirm).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel) {
            dismiss();

        } else if (i == R.id.confirm) {
            if (callback != null) {
                callback.call(this);
            }

        }
    }
}
