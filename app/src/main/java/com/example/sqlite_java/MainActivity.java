package com.example.sqlite_java;

import androidx.appcompat.app.AppCompatActivity;

        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText ed_book,ed_price;
    private Button btn_query,btn_insert,btn_update,btn_delete;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_book = findViewById(R.id.ed_book);
        ed_price = findViewById(R.id.ed_price);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
        dbrw = new MyDBHelper(this).getWritableDatabase();

        btn_query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor c;
                if(ed_book.length() < 1){
                    c = dbrw.rawQuery("SELECT * FROM myTable",null);
                }
                else {
                    c = dbrw.rawQuery("SELECT * FROM myTable WHERE book LIKE '" + ed_book.getText().toString() + "'",null);
                }
                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this,"共有" + c.getCount() + "筆資料",Toast.LENGTH_SHORT).show();

                for(int i = 0;i < c.getCount();i++){
                    items.add("書名:" + c.getString(0) + "\t\t\t\t價格:" + c.getString(1));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ed_book.length()<1 || ed_price.length()<1){
                    Toast.makeText(MainActivity.this, "欄位請勿留空",Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        dbrw.execSQL("INSERT INTO myTable(book,price) VALUES(?,?)",new Object[]{ed_book.getText().toString(),ed_price.getText().toString()});
                        Toast.makeText(MainActivity.this,"新增書名"+ed_book.getText().toString() + "價格" +ed_price.getText().toString(),Toast.LENGTH_SHORT).show();

                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"新增失敗:" + e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ed_book.length()<1 || ed_price.length()<1){
                    Toast.makeText(MainActivity.this, "欄位請勿留空",Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        dbrw.execSQL("UPDATE myTable SET price = " + ed_price.getText().toString() + " WHERE book LIKE '" + ed_book.getText().toString() + "'");
                        Toast.makeText(MainActivity.this,"更新書名"+ed_book.getText().toString() + "價格" +ed_price.getText().toString(),Toast.LENGTH_SHORT).show();

                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"更新失敗:" + e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ed_book.length()<1){
                    Toast.makeText(MainActivity.this, "書名請勿留空",Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '" + ed_book.getText().toString() + "'");
                        Toast.makeText(MainActivity.this,"刪除書名"+ed_book.getText().toString(),Toast.LENGTH_SHORT).show();

                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"刪除失敗:" + e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbrw.close();
    }
}