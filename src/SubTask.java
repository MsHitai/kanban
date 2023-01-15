import java.util.Objects;

public class SubTask extends Task {
    protected int myEpicID;
    boolean isDone;

    String name;
    String description;
    int uniqueID;

    public SubTask(String name, String description, int uniqueID, String status, boolean isDone, int myEpicID) {
        super(name, description, uniqueID, status, isDone);
        this.myEpicID = myEpicID;
        if (status.equals("DONE")) {
            this.isDone = true;
        }
        this.name = name;
        this.description = description;
        this.uniqueID = uniqueID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return myEpicID == subTask.myEpicID
                && uniqueID == subTask.uniqueID
                && name.equals(subTask.name)
                && description.equals(subTask.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myEpicID, name, description, uniqueID);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "myEpicID=" + myEpicID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
