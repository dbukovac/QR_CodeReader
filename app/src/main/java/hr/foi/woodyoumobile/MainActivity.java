package hr.foi.woodyoumobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

/**
 * Klasa glavne aktivnosti koja se pokreće prilikom otvaranja
 * aplikacije. Služi za ispis i odabir projekata.
 */
public class MainActivity extends AppCompatActivity {

    private Project selectedProject = null;

    /**
     * Metoda koja se poziva kada se kreira glavna aktivnost.
     * Pokreće se AsyncTask za dohvaćanje projektata
     * @param savedInstanceState
     */
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

    @Override
    protected void onResume() {
        super.onResume();
        try {
            new GetProjectsTask().execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ugniježđena klasa koja služi za dohvaćanje podataka o aktivnim i nezavršenim
     * projektima. Naslijeđuje klasu AsyncTask.
     */
    private class GetProjectsTask extends AsyncTask<Void, Void, ArrayList<Project>>
    {
        /**
         * Metoda koja dohvaća projekte aktivne i nezavršene projekte,
         * te kreira objekte tipa Project i dodaje ih u listu projekata.
         *
         * @param voids
         * @return          Lista objekata tipa Project.
         */
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

        /**
         * Metoda koja se izvršava nakon pokretanja AsyncTask-a.
         * Služi za kreiranje i prikaz View-a za prikaz projekata,
         * te postavlja metodu koja se poziva na klik tipke phasesButton
         * (otvara aplikaciju za skeniranje QR koda)
         *
         * @param projects          Lista objekata tipa Project
         */
        @Override
        protected void onPostExecute(ArrayList<Project> projects)
        {
            LinearLayout linearLayout = findViewById(R.id.projectLinearLayout);
            linearLayout.removeAllViews();
            for(final Project project: projects) {
                View projectsView = LayoutInflater.from(MainActivity.this).inflate(R.layout.project_view, null);
                TextView projectTextView = projectsView.findViewById(R.id.projectTextView);
                projectTextView.setText(getResources().getString(R.string.projectName)+" "+project.getName()+"\n"+getResources().getString(R.string.partnerName)+" "+project.getPartnerName());

                Button phasesButton = projectsView.findViewById(R.id.phasesButton);

                phasesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            selectedProject = project;
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

    /**
     * Metoda koja provjerava rezultat skeniranja QR koda.
     * Ispisuje poruku pogreške ukoliko QR kod ne sadrži potrebne podatke.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                    intent.putExtra("PROJECT", selectedProject);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.error))
                            .setMessage(getResources().getString(R.string.nonValidQRCode))
                            .setPositiveButton(getResources().getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
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

    /**
     * Metoda koja provjerava može li se String pretvoriti u Integer
     *
     * @param s         String koji se provjerava
     * @return          True ako se može pretvoriti,
     *                  False ako ne može.
     */
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

