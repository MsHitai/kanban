import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<SubTask> mySubTasks;
    String status;

    public Epic(String name, String description, int uniqueID, String status, boolean isDone) {
        super(name, description, uniqueID, status, isDone);
    }

    public void fillMySubTasks(ArrayList<SubTask> subTasks) {
        mySubTasks = subTasks;
    }

    public boolean checkIsDone() {
        if (mySubTasks != null) {
            int total = mySubTasks.size();
            for (SubTask mySubTask : mySubTasks) {
                if (mySubTask.isDone) {
                    total--;
                }
            }
            isDone = total == 0;
        } else {
            System.out.println("Подзадач для этого эпика нет.");
        }
        return isDone;
    }

    private boolean allIsNew() {
        boolean check = false;
        if (mySubTasks != null) {
            int total = mySubTasks.size();
            for (SubTask mySubTask : mySubTasks) {
                if (mySubTask.status.equals("NEW")) {
                    total--;
                }
            }
            check = total == 0;
        } else {
            System.out.println("Подзадач для этого эпика нет.");
        }
        return check;
    }

    public String checkStatus() {
        if (mySubTasks == null) {
            this.status = "NEW";
        } else {
            if (checkIsDone()) {
                this.status = "DONE";
            } else {
                if (allIsNew()) {
                    this.status = "NEW";
                } else {
                    this.status = "IN_PROGRESS";
                }
            }
        }
        return this.status;
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
