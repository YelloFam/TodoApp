package com.yellofam.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private String result;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        if (items.isEmpty()){
            items.add("First Item");
            items.add("Second Item");
        }
        setupListViewListener();
        setupIntentListener();
    }

    public void onAddItem (View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (itemText.length()> 0 && !itemText.trim().isEmpty()) {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems(items);
        }
    }

    private final int REQUEST_CODE = 20;

    private void setupIntentListener () {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        //String editedItem = onEditItem(item, pos);
                        //items.set(pos, editedItem);
                        //itemsAdapter.notifyDataSetChanged();
                        //writeItems(items);
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        String itu = items.get(pos);
                        bundle.putString("key", itu);
                        bundle.putInt("id", (int) id);
                        i.putExtras(bundle);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                });
    }

    public String onEditItem (View v, int pos){
        Intent i = new Intent(this, EditItemActivity.class);
        String item = items.get(pos);
        i.putExtra("key", item);

        startActivityForResult(i, REQUEST_CODE);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        return result; //intent.getDataString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            result = data.getExtras().getString("edittextvalue");
            int pos = data.getExtras().getInt("id");
            //Toast.makeText(MainActivity.this, data.getExtras().getString("id"), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity.this, pos, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            if (result != null && result != items.get(pos)){
                items.set(pos, result);
                itemsAdapter.notifyDataSetChanged();
                writeItems(items);
            }
        }
    }

    private void setupListViewListener () {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems(items);
                        return true;
                    }
                });
    }

    private ArrayList readItems () {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        ArrayList<String> loadedItems;
        try {
            loadedItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            loadedItems = new ArrayList<String>();
        }
        return loadedItems;
    }

    private void writeItems (ArrayList savedItem) {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, savedItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
