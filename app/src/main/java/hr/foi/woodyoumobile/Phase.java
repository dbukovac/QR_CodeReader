package hr.foi.woodyoumobile;

class Phase {
    private int projectId = -1;
    private String name = null;
    private String projectName = null;
    private int phaseId = -1;
    private int phaseProjectId = -1;

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

    public int getPhaseId() { return phaseId; }

    public void setPhaseProjectId(int phaseProjectId) { this.phaseProjectId = phaseProjectId; }

    public int getPhaseProjectId() { return  phaseProjectId; }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public String getProjectName() {
        return projectName;
    }
}
