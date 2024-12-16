package Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}' + "\n";
    }

}
