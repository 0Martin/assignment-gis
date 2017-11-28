package com.example.martin.mapbox;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

public class LengthActivity extends Activity {
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.length_activity);

        editText = (EditText) findViewById(R.id.editText);
    }

    public void setLength(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",Integer.parseInt(editText.getText().toString()));
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
