public class Epic extends Task{

    public Epic(String name, String description, int uniqueID, String status, boolean isDone) {
        super(name, description, uniqueID, status, isDone);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
