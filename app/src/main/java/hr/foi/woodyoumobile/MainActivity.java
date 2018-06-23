package hr.foi.woodyoumobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

public class MainActivity extends AppCompatActivity {

    public Integer projectId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            new GetProjectsTask().execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GetProjectsTask extends AsyncTask<Void, Void, ArrayList<Project>>
    {
        @Override
        protected ArrayList<Project> doInBackground(Void ... voids)
        {
            ArrayList<Project> projects = new ArrayList<Project>();

            DbConnection.getInstance().openConnection();

            try
            {
                ResultSet resultSet =  DbConnection.getInstance().executeQuery("SELECT * FROM ProjektView;");

                while (resultSet.next())
                {
                    Project project = new Project();
                    project.setProjectId(resultSet.getInt("projektId"));
                    project.setName(resultSet.getString("ime"));
                    project.setPartnerName(resultSet.getString("imePartner"));

                    projects.add(project);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                DbConnection.getInstance().closeConnection();
            }

            return projects;
        }

        @Override
        protected void onPostExecute(ArrayList<Project> projects)
        {
            LinearLayout linearLayout = findViewById(R.id.projectLinearLayout);
            for(final Project project: projects) {
                View projectsView = LayoutInflater.from(MainActivity.this).inflate(R.layout.project_view, null);
                TextView projectTextView = projectsView.findViewById(R.id.projectTextView);
                projectTextView.setText(getResources().getString(R.string.projectName)+" "+project.getName()+"\n"+getResources().getString(R.string.partnerName)+" "+project.getPartnerName());

                Button phasesButton = projectsView.findViewById(R.id.phasesButton);

                phasesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            projectId = project.getProjectId();
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                            startActivityForResult(intent, 0);
                        } catch (Exception e) {
                            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            startActivity(marketIntent);
                        }
                    }
                });
                linearLayout.addView(projectsView);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("SCAN_RESULT");
                if(isNumeric(result)) {
                    Log.w("QR_CODE", result);
                    Intent intent = new Intent(MainActivity.this, DisplayPhaseActivity.class);
                    intent.putExtra("PHASE_ID", result);
                    intent.putExtra("PROJECT_ID", projectId.toString());
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Greška!")
                            .setMessage("Nevažeći QR kod!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    alertDialogBuilder.show();
                }

            }
            if(resultCode == RESULT_CANCELLED){

            }
        }
    }

    private boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}

