package hr.foi.woodyoumobile;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

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

            DbConnection dbConnection = new DbConnection();

            dbConnection.openConnection();

            try
            {
                Statement statement = dbConnection.connection.createStatement();
                ResultSet resultSet =  statement.executeQuery("SELECT * FROM Projekt WHERE (aktivan = 1) AND (gotovo = 0);");

                while (resultSet.next())
                {
                    Project project = new Project();
                    project.setProjectId(resultSet.getInt("projektId"));
                    project.setName(resultSet.getString("ime"));
                    project.setPartnerId(resultSet.getInt("partnerId"));

                    projects.add(project);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                dbConnection.closeConnection();
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
                projectTextView.setText("Project ID: "+project.getProjectId()+"\nProject name: "+project.getName());

                Button phasesButton = projectsView.findViewById(R.id.phasesButton);

                phasesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Hello World!")
                                .setMessage("Stisnuli ste ID: "+project.getProjectId())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.show();
                    }
                });
                linearLayout.addView(projectsView);
            }
        }
    }
}

