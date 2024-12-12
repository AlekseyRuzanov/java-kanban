package Tasks;

public class SubTask extends Task {
    protected Integer linkedEpicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public void setlinkedEpicId(Integer epicId) {
        linkedEpicId = epicId;
    }

    public Integer getLinkedEpicId() {
        return linkedEpicId;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}' + "\n";
    }
}
