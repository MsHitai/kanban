import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<SubTask> mySubTasks;

    public Epic(String name, String description, int uniqueID, String status, boolean isDone) {
        super(name, description, uniqueID, status, isDone);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'' +
                ", isDone=" + isDone;
        if (mySubTasks != null) {
            result = result + ", mySubTasks=" + mySubTasks.size() + '}';
        } else {
            result = result + ", mySubTasks=null" + '}';
        }
        return result;
    }
}
