package com.example.android.notepad.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smaug(Qiuzhonghao) on 2017/5/18.
 */

public class NoteCaretaker {
    //最大存储量
    private static final int MAX = 30;
    List<Memento> mMementoList = new ArrayList<>(MAX);

    int mIndex = 0;//索引

    public void saveMemento(Memento memento) {
        if (mMementoList.size() > MAX) {
            mMementoList.remove(0);//存满之后从第一条开始删
        } else {
            mMementoList.add(memento);
            mIndex = mMementoList.size() - 1;
        }
    }

    //获取上一个存档信息
    public Memento getPrevMemento() {
        mIndex = mIndex > 0 ? --mIndex : 0;
        return mMementoList.get(mIndex);
    }

    //重做
    public Memento getNextMemento() {
        mIndex = mIndex < mMementoList.size() - 1 ? ++mIndex : mIndex;
        return mMementoList.get(mIndex);
    }

}
