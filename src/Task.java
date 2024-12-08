public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected Integer id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}' + "\n";
    }
}
