package programming146.fernsteuerung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //allow MainActivity use of network in main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        refreshGoals();

        final EditText commandInput = (EditText) findViewById(R.id.main_command_input);
        final TextView commandLog = (TextView) findViewById(R.id.main_command_log);
        final TextView addressee = (TextView) findViewById(R.id.main_addressee);
        Button commandStrg = (Button) findViewById(R.id.main_command_strg);
        Button commandStrgRelease = (Button) findViewById(R.id.main_command_strg_release);
        Button commandAlt = (Button) findViewById(R.id.main_command_alt);
        Button commandAltRelease = (Button) findViewById(R.id.main_command_alt_release);
        Button commandWindows = (Button) findViewById(R.id.main_command_windows);
        Button commandWindowsRelease = (Button) findViewById(R.id.main_command_windows_release);
        Button commandSlash = (Button) findViewById(R.id.main_command_slash);
        Button commandSlash2 = (Button) findViewById(R.id.main_command_slash2);
        Button commandSend = (Button) findViewById(R.id.main_command_send);

        commandLog.setMovementMethod(new ScrollingMovementMethod());

        commandStrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.strg);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commandStrgRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.strgRelease);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commandAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.alt);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commandAltRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.altRelease);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commandWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.windows);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commandWindowsRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.windowsRelease);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commandSlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.slash);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });
        commandSlash2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString() + getString(R.string.slash2);
                commandInput.setText(command);
                commandInput.setSelection(commandInput.length());
            }
        });

        commandSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandInput.getText().toString();
                String[] request = new String[]{"setCommand", addressee.getText().toString(),"keyboard",command};
                ServerConnection serverConnection = new ServerConnection(getApplicationContext());
                JSONObject result = serverConnection.doInBackground(request);

                String status = "";
                try{
                    if (result != null) {
                        status = result.getString("status");
                        if(!status.equals("200")){
                            command = result.getString("error");
                        }
                    }
                }catch (JSONException e){
                    Log.d("146s",e.toString());
                }
                String output = commandLog.getText().toString() + "\n";
                Integer temp = Calendar.getInstance().get(Calendar.HOUR);
                output += correctTimeFormat(temp) + ":";
                temp = Calendar.getInstance().get(Calendar.MINUTE);
                output += correctTimeFormat(temp) + ":";
                temp = Calendar.getInstance().get(Calendar.SECOND);
                output += correctTimeFormat(temp);
                output += "   " + status + "   " + command;
                commandLog.setText(output);
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

            case R.id.action_refresh:
                //User chose the "Refresh" item, refresh the goal layout
                refreshGoals();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void refreshGoals(){
        TextView author = (TextView) findViewById(R.id.main_author);
        TextView addressee = (TextView) findViewById(R.id.main_addressee);

        SharedPreferences preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        author.setText(preferences.getString("username","no login data"));
        addressee.setText(preferences.getString("addressee", "no addressee"));
    }
    public String correctTimeFormat(Integer input){
        String output ="";
        if(input <10){
            output = "0";
        }
        output+=input;
        return output;
    }
}
