package com.pb.criconet.util;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.pb.criconet.R;
import com.pb.criconet.fontsview.OSLTextView;

import java.util.ArrayList;

public class DropDownBlue extends OSLTextView implements View.OnClickListener, PopupWindow.OnDismissListener {

    private ArrayList<DataModel> optionList = new ArrayList<>();
    private onClickInterface onClickInterface;

    public DropDownBlue(Context context) {
        super(context);
        initView(context);
    }

    public DropDownBlue(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DropDownBlue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private PopupWindow popupWindowSort(Context context) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow();
        popupWindow.setWidth(this.getWidth());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(context.getDrawable(R.drawable.blue_drop_down_bg));
        popupWindow.setElevation(10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.setOverlapAnchor(true);
        }
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, options);
        CustomAdapter adapter = new CustomAdapter(optionList, context);
        // the drop down list is a list view
        ListView listViewSort = new ListView(context);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener((parent, view, position, id) -> {
            DataModel model = optionList.get(position);
            this.setText(model.getName());
            if (onClickInterface != null)
                onClickInterface.onClickDone(model.getName());
            popupWindow.dismiss();
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        if (optionList.size() > 10)
            popupWindow.setHeight(480);
        else
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(this);

        // set the ListView as popup content
        popupWindow.setContentView(listViewSort);

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getRootView().getWindowToken(), 0);
        getRootView().clearFocus();


        return popupWindow;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v == this) {
            PopupWindow window = popupWindowSort(v.getContext());
            window.showAsDropDown(v, 0, 0);
            if (onClickInterface != null)
                onClickInterface.onClickAction();
        }
    }

    public void setOptionList(ArrayList<DataModel> list) {
        this.optionList = list;
    }

    public void setClickListener(onClickInterface listener) {
        this.onClickInterface = listener;
    }

    @Override
    public void onDismiss() {
        if (onClickInterface != null)
            onClickInterface.onDismiss();
    }

    public interface onClickInterface {
        void onClickAction();

        void onClickDone(String name);

        void onDismiss();
    }

    public class CustomAdapter extends ArrayAdapter<DataModel> implements OnClickListener {

        private ArrayList<DataModel> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            int id;
            TextView txtName;
        }

        CustomAdapter(ArrayList<DataModel> data, Context context) {
            super(context, R.layout.academy_drop_down_item, data);
            this.dataSet = data;
            this.mContext = context;
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            DataModel object = getItem(position);

        }

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            DataModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.academy_drop_down_item, parent, false);
                viewHolder.txtName = convertView.findViewById(R.id.tv_language);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            assert dataModel != null;
            viewHolder.txtName.setText(dataModel.getName());
            // Return the completed view to render on screen
            return convertView;
        }

    }
}

