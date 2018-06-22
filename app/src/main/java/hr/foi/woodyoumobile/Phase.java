package hr.foi.woodyoumobile;

class Phase {
    private int projectId;
    private String name;
    private int phaseId;

    public Phase() {
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

    public void setPhaseId(int partnerId) {
        this.phaseId = partnerId;
    }

    public int getPhaseId() {
        return phaseId;
    }
}
