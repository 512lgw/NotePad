package com.example.android.notepad.memento;

/**
 * Created by smaug(Qiuzhonghao) on 2017/5/18.
 * 该天习得备忘录模式，故：
 * 该部分参考备忘录模式 目的在于可实现笔记输入过程中的撤销。
 * 源工程中已经含有撤销功能，但是只是针对于一次性撤销，即将该日记重置为打开前的状态
 * 我想实现的撤销在于可以记录你的每一次“点击保存”前的状态，这样在写长文本日记的时候比较方便，不用一步推倒，重新来过。
 * 代码已经完全实现，但是未整合到源工程中，先放着吧。
 */

public class Memento {
    private String text;
    private int cursor;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }
}
