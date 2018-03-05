package programming146.fernsteuerung;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by FM on 07.02.2018.
 * set settings for command creation and server connection
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText username = (EditText) findViewById(R.id.editTextUsername);
        final EditText password = (EditText) findViewById(R.id.editTextPassword);
        final EditText addressee = (EditText) findViewById(R.id.editTextAddressee);
        Button saveButton = (Button) findViewById(R.id.buttonSaveSettings);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("username",username.getText().toString());
                editor.putString("password",password.getText().toString());
                editor.putString("addressee",addressee.getText().toString());

                editor.apply();

                Toast toast = Toast.makeText(getApplicationContext(),"saved settings", Toast.LENGTH_SHORT);
                toast.show();

                //TODO: refresh goal layout
                //MainActivity.refreshGoals();

                finish();
            }
        });
    }
}
