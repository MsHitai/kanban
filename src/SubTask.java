public class SubTask extends Task{
    protected int myEpicID;

    public SubTask(String name, String description, int uniqueID, String status, boolean isDone, int myEpicID) {
        super(name, description, uniqueID, status, isDone);
        this.myEpicID = myEpicID;
        if (status.equals("DONE")){
            isDone = true;
        }
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
