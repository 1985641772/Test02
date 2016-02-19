package com.qianfeng.webviewdemo.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.qianfeng.webviewdemo.R;

/**
 * Created by WangChao.
 *
 * @date : 16-2-18.
 */
public class EditTextWithDel extends EditText
{

    private Drawable delNoDark;
    private Drawable dark;
    private Context mContext;




    public EditTextWithDel(Context context)
    {
        super(context);
        mContext = context;
        init();

    }

    public EditTextWithDel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init()
    {
        dark = mContext.getResources().getDrawable(R.drawable.delete);
        delNoDark = mContext.getResources().getDrawable(R.drawable.delete_gray);
        addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                setDrawable();
            }
        });
    }

    private void setDrawable()
    {
        if(length() == 0)
        {
            setCompoundDrawablesWithIntrinsicBounds(null, null, delNoDark, null);
        } else
        {
            setCompoundDrawablesWithIntrinsicBounds(null, null, dark, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            int pointX = (int) event.getRawX();
            int pointY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if(rect.contains(pointX, pointY))
            {
                setText("");
            }
        }
        return super.onTouchEvent(event);

    }
}
