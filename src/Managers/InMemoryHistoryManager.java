package Managers;

import Exceptions.InvalidInputException;
import Tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static List<Task> history = new LinkedList<>();

    @Override
    public void addTask(Task task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.removeFirst();
            history.add(task);
        }
    }

    @Override
    public void clearHistory() {
        history.clear();
    }

    @Override
    public List<Task> getHistory() throws InvalidInputException {
        if (history.isEmpty()) {
            throw new InvalidInputException("История просмотров пуста");
        }
        return history;
    }

}
