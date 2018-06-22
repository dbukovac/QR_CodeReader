package hr.foi.woodyoumobile;

public class Project {

    private int projectId;
    private String name;
    private String partnerName;

    public Project() {
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerName() {
        return partnerName;
    }
}
