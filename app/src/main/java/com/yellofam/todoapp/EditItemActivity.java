package com.yellofam.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends AppCompatActivity {

    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Item");
        setContentView(R.layout.activity_edit_item);

        Intent intent = getIntent();
        String message = intent.getExtras().getString("key");
        EditText editItem = (EditText)findViewById(R.id.etEditItem);
        editItem.setText(message);

        // forward position back to Main activity
        bundle.putInt("id", intent.getExtras().getInt("id"));
    }

    public void onUpdateItem (View v) {
        EditText editItem = (EditText)findViewById(R.id.etEditItem);
        bundle.putString("edittextvalue", editItem.getText().toString());
        Intent data = new Intent();
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }
}
