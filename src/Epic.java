import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> epicSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getEpicSubTasks() {
        return epicSubTasks;
    }

    public void setEpicSubTasks(List<Integer> newEpicSubTasks) {
        epicSubTasks = newEpicSubTasks;
    }

    public void addEpicSubTasks(Integer subTaskId) {
        epicSubTasks.add(subTaskId);
    }

    public void deleteEpicSubTasks(Integer id) {
        epicSubTasks.remove(id);
    }

    public void deleteAllEpicSubTasks() {
        epicSubTasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}' + "\n";
    }
}
