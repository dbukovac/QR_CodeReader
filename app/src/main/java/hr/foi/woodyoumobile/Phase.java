package hr.foi.woodyoumobile;

import java.io.Serializable;

/**
 * Klasa koja služi za spremanje podataka o fazi
 */
public class Phase implements Serializable{
    private int projectId = -1;
    private String name = null;
    private String projectName = null;
    private int phaseId = -1;
    private int phaseProjectId = -1;
    private Project project = null;

    public Phase() {
    }

    /**
     * Postavlja ID projekta vezanog uz fazu
     *
     * @param projectId         ID projekta
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Dohvaća ID projekta vezanog uz fazu
     *
     * @return          ID projekta
     */
    public int getProjectId() {
        return this.projectId;
    }

    /**
     * Postavlja ime faze
     *
     * @param name      Ime faze
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dohvaća ime faze
     *
     * @return      Ime faze
     */
    public String getName() {
        return name;
    }

    /**
     * Postavlja ID faze
     *
     * @param phaseId         ID faze
     */
    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    /**
     * Dohvaća ID faze
     *
     * @return          ID faze
     */
    public int getPhaseId() { return phaseId; }

    /**
     * Postavlja ID faze na projektu
     *
     * @param phaseProjectId        ID faze na projektu
     */
    public void setPhaseProjectId(int phaseProjectId) { this.phaseProjectId = phaseProjectId; }

    /**
     * Dohvaća ID faze na projetu
     *
     * @return          ID faze na projektu
     */
    public int getPhaseProjectId() { return  phaseProjectId; }

    /**
     * Postavlja ime projekta
     *
     * @param name          Ime projekta
     */
    public void setProjectName(String name) {
        this.projectName = name;
    }

    /**
     * Dohvaća ime projekta
     *
     * @return          Ime projekta
     */
    public String getProjectName() {
        return projectName;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
