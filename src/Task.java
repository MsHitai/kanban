public class Task {
    protected String name;
    protected String description;
    protected int uniqueID;
    protected String status;
    protected boolean isDone;

    public Task(String name, String description, int uniqueID, String status, boolean isDone) {
        this.name = name;
        this.description = description;
        this.uniqueID = uniqueID;
        this.status = status;
        this.isDone = isDone;

        if (status.equals("DONE")){
            this.isDone = true;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
