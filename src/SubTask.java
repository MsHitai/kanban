public class SubTask extends Epic{

    public SubTask(String name, String description, int uniqueID, String status, boolean isDone) {
        super(name, description, uniqueID, status, isDone);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
