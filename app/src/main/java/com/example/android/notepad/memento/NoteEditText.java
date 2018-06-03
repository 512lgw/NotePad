package com.example.android.notepad.memento;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by smaug(Qiuzhonghao) on 2017/5/18.
 */

public class NoteEditText extends android.support.v7.widget.AppCompatEditText {
    private Rect mRect;
    private Paint mPaint;


    public NoteEditText(Context context) {
        super(context);
    }

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Creates a Rect and a Paint object, and sets the style and color of the Paint object.
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x800000FF);
    }

    public NoteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Memento mementoFactory() { //
        Memento noteMemento = new Memento();
        noteMemento.setText(getText().toString());
        noteMemento.setCursor(getSelectionStart());
        return noteMemento;
    }

    public void restore(Memento memento) { //撤销
        setText(memento.getText());
        setSelection(memento.getCursor());
    }



    @Override
    protected void onDraw(Canvas canvas) {

        // Gets the number of lines of text in the View.
        int count = getLineCount();

        // Gets the global Rect and Paint objects
        Rect r = mRect;
        Paint paint = mPaint;

            /*
             * Draws one line in the rectangle for every line of text in the EditText
             */
        for (int i = 0; i < count; i++) {

            // Gets the baseline coordinates for the current line of text
            int baseline = getLineBounds(i, r);

                /*
                 * Draws a line in the background from the left of the rectangle to the right,
                 * at a vertical position one dip below the baseline, using the "paint" object
                 * for details.
                 */
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
        }

        // Finishes up by calling the parent method
        super.onDraw(canvas);
    }

}
