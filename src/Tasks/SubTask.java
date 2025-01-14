package Tasks;

import java.util.Objects;

public class SubTask extends Task {
    protected Integer linkedEpicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public void setLinkedEpicId(Integer epicId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(name, subTask.name) && Objects.equals(description, subTask.description) && Objects.equals(status, subTask.status) && Objects.equals(id, subTask.id);
    }
}
