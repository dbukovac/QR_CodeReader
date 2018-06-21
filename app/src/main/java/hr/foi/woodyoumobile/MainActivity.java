package hr.foi.woodyoumobile;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
            for(Project project: projects) {
                TextView projectTextView = new TextView(MainActivity.this);
                projectTextView.setText("Project ID: "+project.getProjectId()+"\nProject name: "+project.getName());
                projectTextView.setId(project.getProjectId());
                projectTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) projectTextView.getLayoutParams();

                marginLayoutParams.bottomMargin=30;
                marginLayoutParams.leftMargin=30;
                marginLayoutParams.topMargin=30;

                projectTextView.setTextSize(21);
                linearLayout.addView(projectTextView);
            }
        }
    }
}

