package hr.foi.woodyoumobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DisplayPhaseActivity extends AppCompatActivity {

    private TextView phaseIdTextView;
    private TextView phaseNameTextView;
    private TextView projectIdTextView;
    private Integer phaseProjectId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phases);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Integer phaseId = Integer.parseInt(extras.getString("PHASE_ID"));
        Integer projectId = Integer.parseInt(extras.getString("PROJECT_ID"));
        phaseIdTextView = findViewById(R.id.phaseIdTextView);
        phaseNameTextView = findViewById(R.id.phaseNameTextView);
        projectIdTextView = findViewById(R.id.projectIdTextView);
        new GetPhaseTask().execute(projectId, phaseId);
    }

    class GetPhaseTask extends AsyncTask<Integer, Void, Phase> {
        @Override
        protected Phase doInBackground(Integer ... integers) {
            DbConnection.getInstance().openConnection();

            Phase phase = null;

            try {
                ResultSet resultSet = DbConnection.getInstance().executeQuery("SELECT * FROM Faze_projektaView WHERE (projektId = "+integers[0]+") AND (fazaId = "+integers[1]+");");

                if (resultSet.next()) {
                    phase = new Phase();
                    phase.setPhaseProjectId(resultSet.getInt("id"));
                    phaseProjectId = resultSet.getInt("id");
                    phase.setProjectId(resultSet.getInt("projektId"));
                    phase.setName(resultSet.getString("naziv"));
                    phase.setPhaseId(resultSet.getInt("fazaId"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbConnection.getInstance().closeConnection();
            }

            return phase;
        }

        @Override
        protected void onPostExecute(final Phase phase) {
            if(phase != null) {
                phaseIdTextView.setText(((Integer) phase.getPhaseId()).toString());
                projectIdTextView.setText(((Integer) phase.getProjectId()).toString());
                phaseNameTextView.setText(phase.getName());
            }
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayPhaseActivity.this)
                        .setTitle("Greška!")
                        .setMessage("Ne postoji skenirana faza na odabranom projektu: ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                alertDialogBuilder.show();
            }
        }
    }

    public void closePhaseButtonClick(View view) {
        if(phaseProjectId != -1) {

        }
    }
}
