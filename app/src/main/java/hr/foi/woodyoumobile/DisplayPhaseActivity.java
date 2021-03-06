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

/**
 * Klasa aktivnosti za prikazivanje podataka o fazi projekta i
 * zatvaranje faze na projektu.
 */
public class DisplayPhaseActivity extends AppCompatActivity {

    private TextView phaseNameTextView;
    private TextView projectNameTextView;
    private Integer phaseProjectId = -1;
    private Phase phase;

    /**
     * Metoda koja se poziva prilikom kreiranja aktivnosti.
     * Inicijaliziraju se potrebni UI elementi, te se
     * dohvaćaju parametri porslijeđeni iz glavne aktivnosti.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phases);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Integer phaseId = Integer.parseInt(extras.getString("PHASE_ID"));
        Project project = (Project) extras.getSerializable("PROJECT");
        phaseNameTextView = findViewById(R.id.phaseNameTextView);
        projectNameTextView = findViewById(R.id.projectNameTextView);
        new GetPhaseTask().execute(project, phaseId);
    }

    /**
     * Ugniježđena klasa koja naslijeđuje AsyncTask klasu.
     * Služi za dohvaćanje podataka o fazi u drugoj dretvi kako se ne bi
     * blokirala glavna dretva.
     */
    private class GetPhaseTask extends AsyncTask<Object, Void, Phase> {
        /**
         * Metoda kojom se dohvaćaju podaci o fazi projekta iz baze podataka.
         * Ukoliko postoji više istih faza na projektu, uzima se prva dohvaćena faza.
         *
         * @param objects      Polje objekata prvi i drugi element polja
         *                      su objekt tipa Projekt i ID faze tipa Integer
         * @return              Objekt tipa Phase
         */
        @Override
        protected Phase doInBackground(Object ... objects) {
            DbConnection.getInstance().openConnection();

            Project project = (Project) objects[0];
            Integer phaseId = (Integer) objects[1];

            try {
                ResultSet resultSet = DbConnection.getInstance().executeQuery("SELECT * FROM Faze_projektaView WHERE (projektId = "+project.getProjectId()+") AND (fazaId = "+phaseId+");");

                if (resultSet.next()) {
                    phase = new Phase();
                    phase.setPhaseProjectId(resultSet.getInt("id"));
                    phaseProjectId = resultSet.getInt("id");
                    phase.setProject(project);
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

        /**
         * Metoda koja se pokreće nakon pokretanja AsyncTaska,
         * služi za ažuriranje UI elemenata na aktivnosti, odnosno
         * prikaz poruke greške.
         *
         * @param phase     Objekt tipa Phase, ako nije null, ispisju se
         *                  podaci o objektu,a ako je null ispisuje se poruka
         *                  greška.
         */
        @Override
        protected void onPostExecute(final Phase phase) {
            if(phase != null) {
                projectNameTextView.append(" "+phase.getProject().getName());
                phaseNameTextView.append(" "+phase.getName());
            }
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayPhaseActivity.this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.nonValidPhase))
                        .setPositiveButton(getResources().getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                alertDialogBuilder.show();
            }
        }
    }

    /**
     * Metoda koja služi za zatvaranje faze na projektu, poziva se na
     * pritisak tipke "closePhaseButton". Ispisuje poruku upozorenja,
     * i ako se odgovori potvrdno, zatvara se faza na projektu, te se
     * vraća na početnu aktivnost
     * @param view      View iz kojeg se pozvala metoda.
     */
    public void closePhaseButtonClick(View view) {
        if(phase.getPhaseProjectId() != -1) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayPhaseActivity.this)
                    .setTitle(getResources().getString(R.string.warning))
                    .setMessage(getResources().getString(R.string.lockPhase))
                    .setPositiveButton(getResources().getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DbConnection.getInstance().openConnection();
                                    DbConnection.getInstance().executeUpdate("UPDATE Faze_projekta SET zakljucano = 1 WHERE id = "+phase.getPhaseProjectId()+";");
                                    DbConnection.getInstance().closeConnection();
                                }
                            });
                            thread.start();
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.negativeButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            alertDialogBuilder.show();

        }
    }
}
