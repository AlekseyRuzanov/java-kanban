public class SubTask extends Task {
    protected Integer linkedEpicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    protected void setlinkedEpicId(Integer id) {
        linkedEpicId = id;
    }

    public Integer getLinkedEpicId() {
        return linkedEpicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}' + "\n";
    }
}
