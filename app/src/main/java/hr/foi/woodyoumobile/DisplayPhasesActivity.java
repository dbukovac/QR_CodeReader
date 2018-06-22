package hr.foi.woodyoumobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayPhasesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phases);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String projectId = extras.getString("PROJECT_ID");
        TextView textView = findViewById(R.id.projectIdTextView);
        textView.setText(projectId);
    }
}
