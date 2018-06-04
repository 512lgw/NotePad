# NotePad
## 1.基本功能


###  ①.时间戳显示
![](https://github.com/SOURAI09/NotePad/blob/master/img/8f1f9481ab28ba983668ec852dacbfb.png)

  笔记**创建**和**修改**的时间戳已经在数据库中保存，我直接把数据库查询结果通过 ```SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa")```将式转换为年月日时分秒AM/PM形式保存，然后插入数据库

  查询到**SimpleCursorAdapter源码**如下，发现要在**from**和**to**中分别添加进**时间戳的数据列对应名**以及我要显示在**xml中的对应资源ID**

  ```
  //源码
  public SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
      super(context, layout, c);
      mTo = to;
      mOriginalFrom = from;
      findColumns(c, from);
  }
  ```
  ```
   //from 第二项为增加项 下同
   String[] dataColumns = {NotePad.Notes.COLUMN_NAME_TITLE,  NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE};

   //to
   int[] viewIDs = {android.R.id.text1, R.id.tv_time_stamp};
  ```
更改适配器之后，成功地显示了时间戳
#### 接着我在NodeEditor中的```updateNote ```方法中继续进行时间戳格式转换以确保在被笔记修改时，时间戳会相应改变。

####  部分代码
  ```
  private final void updateNote(String text, String title) {

     Toast.makeText(this, "笔记修改", Toast.LENGTH_SHORT).show();
     long now = System.currentTimeMillis();
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");  
     String nowTime=sdf.format(new Date(now));

     // Sets up a map to contain values to be updated in the provider.
     ContentValues values = new ContentValues();
     values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, nowTime);
  ```
###  ②.按标题查询功能
## 提供两种模式
### 1.精确搜索：实现了输入准确标题弹出该笔记的功能。
![](https://github.com/SOURAI09/NotePad/blob/master/img/05d0dbe3c310b10ff54953715d7ec4f.png)
![](https://github.com/SOURAI09/NotePad/blob/master/img/yyp.png)

 #### 实现思路：我模仿原项目中Listview的item点击跳转，先查询全部结果，最后从结果中**获取标题对应列**的值与**输入的标题**匹配，返回匹配项的**id**（即数据库的第一列 主键_id），通过```ContentUris.withAppendedId```添加到uri末尾。

#### 数据库结构如下 ####

| _id    | title  |note |created|modified|
| :------------- | :------------- |:-- |:-- |:--|
| 1              | 笔记1号     | 这个内容是我瞎编的| 1494478104       |1494478104
|2|笔记2号| 没有啊 |1494478100|1494478104|


```
  @TargetApi(Build.VERSION_CODES.KITKAT)
    private Boolean doSearch(String title) { //查询标题是否存在
        String[] TitleselectionArgs = {title};

        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, NotePad.Notes
                .DEFAULT_SORT_ORDER);//查询全部结果
        mUri = cursor.getNotificationUri();
        cursor.moveToFirst();
        do {
            if (title.equals(cursor.getString(cursor.getColumnIndex("title")))) {
                mUri = ContentUris.withAppendedId(mUri, cursor.getInt(0));
                // Toast.makeText(this,  cursor.getInt(0)+"?"+cursor.getString(1),
                // Toast.LENGTH_LONG).show();
                cursor.close();
                return true;
            }
        } while (cursor.moveToNext());
        cursor.close();
        return false;
    }
 ```
### 2.模糊搜索
![](https://github.com/SOURAI09/NotePad/blob/master/img/1528081133(1).png)

![](https://github.com/SOURAI09/NotePad/blob/master/img/%E6%A8%A1%E7%B3%8A%E4%BB%8E%E6%9F%A5%E8%AF%A2.png)

##### 出现以y开头的搜索结果 

![](https://github.com/SOURAI09/NotePad/blob/master/img/%E6%A8%A1%E7%B3%8A%E7%BB%93%E6%9E%9C.png)

#### 实现思路

该功能附加在NotesList的menu中,通过输入标题，然后以" like ？"语句与通配符"%"查询得到结果.构造并设置一个新的适配器，将查询结果显示在listview中。

#### 注意细节 查询结果必须包含自增长列名"_id" 或者将某列：例如id，映射为_id 代码示例："id as_id"。否则报错
```
 java.lang.IllegalArgumentException: column '_id' does not exist
```

#### 代码如下

```
final EditText et = new EditText(this);
               new AlertDialog.Builder(NotesList.this).setTitle("输入查询").setView(et)
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       input_word = et.getText().toString();
                       String[] search = {"_id", NotePad.Notes.COLUMN_NAME_TITLE, NotePad.Notes
                               .COLUMN_NAME_CREATE_DATE};
                       String selection = NotePad.Notes.COLUMN_NAME_TITLE + " like?";
                       String[] selectionArgs = {input_word + "%"};
                       Cursor cursors = managedQuery(getIntent().getData(), search, selection,
                               selectionArgs, NotePad.Notes.DEFAULT_SORT_ORDER);
                       cursors.moveToFirst();
                       String[] data = {NotePad.Notes.COLUMN_NAME_TITLE, NotePad.Notes
                               .COLUMN_NAME_CREATE_DATE};
                       final int[] viewID = {android.R.id.text1, R.id.tv_time};
                       SimpleCursorAdapter adapters = new SimpleCursorAdapter(NotesList.this,
                               R.layout.noteslist_item,
                               cursors,
                               data, viewID);
                       setListAdapter(adapters);
                   }
               }).show();

```
#### tip ： 在模糊搜索完之后没有清空搜索结果，再点击精准查询，查询结果仅在模糊搜索结果之中查询。 清空方式为：在模糊搜索中不输入任何字符直接点确定.
  功能演示如下

### 2.UI美化

##### 演示如下

![](https://github.com/que123567/NotePad/blob/master/app/src/main/res/drawable/noteslist.png)

###  3.按步撤销功能

#####  源工程中已经含有撤销功能，但是只是针对于一次性撤销，即将该日记重置为打开前的状态。我实现的撤销在于可以记录你的每一次“点击保存”前的状态，这样在写长文本日记的时候比较方便，不用一步推倒，重新来过。

#### BUG提示
##### 如果在刚进入编辑界面的时候未点击保存直接点击撤销，可能会遇到一些问题（闪退或内容成空）。所以：请记得先点击保存

演示如下
##### 撤销前先点击保存记录

![](https://github.com/que123567/NotePad/blob/master/app/src/main/res/drawable/save_before_revert.png)

![](https://github.com/que123567/NotePad/blob/master/app/src/main/res/drawable/revert_1.png)

![](https://github.com/que123567/NotePad/blob/master/app/src/main/res/drawable/revert_2.png)

** 基于备忘录模式，自定义控件NoteEdittext继承自EditText，增加了可以保存历史文本内容的功能 **


```
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

```


```
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
```

```
public class NoteEditText extends android.support.v7.widget.AppCompatEditText {
public Memento mementoFactory() {
      Memento noteMemento = new Memento();
      noteMemento.setText(getText().toString());
      noteMemento.setCursor(getSelectionStart());
      return noteMemento;
  }

  public void restore(Memento memento) { //撤销
      setText(memento.getText());
      setSelection(memento.getCursor());
  }
//省略部分代码，具体实现请看源码
}

```
