public class Epic extends Task {

    public Epic(String name, String description) {
        super(name, description);
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
