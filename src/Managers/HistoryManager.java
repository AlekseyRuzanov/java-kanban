package Managers;

import Exceptions.InvalidInputException;
import Tasks.Task;

import java.util.List;

public interface HistoryManager {
    void addTask(Task task);

    List<Task> getHistory() throws InvalidInputException;

    void clearHistory();
}
