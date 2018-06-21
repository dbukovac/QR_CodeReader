package hr.foi.woodyoumobile;

public class Project {

    private int projectId;
    private String name;
    private int partnerId;

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

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getPartnerId() {
        return partnerId;
    }
}
