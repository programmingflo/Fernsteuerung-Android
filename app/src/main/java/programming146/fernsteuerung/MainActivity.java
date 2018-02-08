package programming146.fernsteuerung;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final EditText commandInput = (EditText) findViewById(R.id.main_command_input);
        TextView commandLog = (TextView) findViewById(R.id.main_command_log);
        final Button commmandStrg = (Button) findViewById(R.id.main_command_strg);
        Button commmandAlt = (Button) findViewById(R.id.main_command_alt);
        Button commmandWindows = (Button) findViewById(R.id.main_command_windows);
        Button commmandSlash = (Button) findViewById(R.id.main_command_slash);
        Button commmandSend = (Button) findViewById(R.id.main_command_send);

        //TODO: improve code
        commmandStrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.strg);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commmandAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.alt);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commmandWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.windows);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commmandSlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.slash);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });

        commmandSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString();
                String[] request = new String[]{"setCommand","keyboard",command};
                ServerConnection serverConnection = new ServerConnection(getApplicationContext());
                serverConnection.doInBackground(request);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
