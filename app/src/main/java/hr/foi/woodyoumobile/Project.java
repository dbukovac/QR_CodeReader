package hr.foi.woodyoumobile;


/**
 * Klasa koja služi za spremanje podataka o projektu
 */
public class Project {

    private int projectId;
    private String name;
    private String partnerName;

    public Project() {
    }

    /**
     * Postavlja ID projekta
     *
     * @param projectId         ID projekta
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Dohvaća ID projekta
     *
     * @return          ID projekta
     */
    public int getProjectId() {
        return this.projectId;
    }

    /**
     * Postavlja ID projekta
     *
     * @param name         Ime projekta
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dohvaća ime projekta
     *
     * @return          Ime projekta
     */
    public String getName() {
        return name;
    }

    /**
     * Postavlja ime partnera
     *
     * @param partnerName         Ime partnera
     */
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    /**
     * Dohvaća ime partnera
     *
     * @return          Ime partnera
     */
    public String getPartnerName() {
        return partnerName;
    }
}
