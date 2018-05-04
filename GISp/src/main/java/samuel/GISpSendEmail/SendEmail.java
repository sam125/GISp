package samuel.GISpSendEmail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import samuel.GISp.R;

public class SendEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Button button = findViewById(R.id.button_mail);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String adress = ((EditText) findViewById(R.id.mail)).getText().toString();
                sendEmail(adress);

            }
        });
    }

    protected void sendEmail(String adress) {
        Log.i("Send email", "");

        String[] TO = {adress};
        //String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "GISp App Windows");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Download the GISp App Windows in the fowling link "+ "https://firebasestorage.googleapis.com/v0/b/gisp-78081.appspot.com/o/GISP.zip?alt=media&token=c5d7bc58-5162-4846-bcec-1564f8819328 "+
                "Thank you for using the GISp.");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendEmail.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


}

