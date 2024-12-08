import java.util.*;


public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Задача 1", "Описание Задачи 1");
        Task task2 = new Task("Задача 2", "Описание Задачи 2");
        Task task3 = new Task("Обновленная Задача 1", "Описание Задачи 1");
        task3.setId(1);
        task3.setStatus(Status.IN_PROGRESS);
        //Проверка метода createTask
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2");
        Epic epic3 = new Epic("Обновленный Эпик 1", "Описание Эпика 1");
        epic3.setId(3);
        epic3.setStatus(Status.DONE);
        Epic epic4 = new Epic("Обновленный Эпик 2", "Описание Эпика 2");
        epic4.setId(4);
        epic4.setStatus(Status.IN_PROGRESS);
        //Проверка метода createEpic
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        SubTask subTask1 = new SubTask("Подзадача 1, Эпик 1", "Описание Подзадачи 1 Эпика 1");
        SubTask subTask2 = new SubTask("Подзадача 2, Эпик 1", "Описание Подзадачи 2 Эпика 1");
        SubTask subTask3 = new SubTask("Подзадача 3, Эпик 2", "Описание Подзадачи 3 Эпика 2");
        SubTask subTask4 = new SubTask("Обновленная Подзадача 1, Эпик 1", "Описание Подзадачи 1 Эпика 1");
        subTask4.setId(5);
        subTask4.setStatus(Status.DONE);
        SubTask subTask5 = new SubTask("Обновленная Подзадача 2, Эпик 2", "Описание Подзадачи 2 Эпика 1");
        subTask5.setId(6);
        subTask5.setStatus(Status.DONE);
        SubTask subTask6 = new SubTask("Обновленная Подзадача 3, Эпик 2", "Описание Подзадачи 3 Эпика 1");
        subTask6.setId(7);
        subTask6.setStatus(Status.IN_PROGRESS);
        //Проверка метода createSubTask
        manager.createSubTask(subTask1, 3);
        manager.createSubTask(subTask2, 3);
        manager.createSubTask(subTask3, 4);

        //Проверка метода getAllEpics
        System.out.println("ТЕСТ 1. Вывод списка всех Эпиков:");
        System.out.println(manager.getAllEpics());
        System.out.println();

        //Проверка метода getAllTasks
        System.out.println("ТЕСТ 2. Вывод списка всех Задач:");
        System.out.println(manager.getAllTasks());
        System.out.println();

        //Проверка метода getAllSubTasks
        System.out.println("ТЕСТ 3. Вывод списка всех Подзадач:");
        System.out.println(manager.getAllSubTasks());
        System.out.println();

        //Проверка метода getSubTaskByEpicId
        System.out.println("ТЕСТ 4. Вывод списка всех Подзадач по номеру Эпика:");
        System.out.println(manager.getSubTaskByEpicId(3));
        System.out.println(manager.getSubTaskByEpicId(4));
        System.out.println();

        //Проверка метода updateEpic и метода getEpicById
        manager.updateEpic(epic3);
        manager.updateEpic(epic4);
        System.out.println("ТЕСТ 5. Вывод списка обновленных Эпиков");
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getEpicById(4));
        System.out.println();

        //Проверка метода updateTask и getTaskById
        manager.updateTask(task3);
        System.out.println("ТЕСТ 6. Вывод обновленной Задачи 1");
        System.out.println(manager.getTaskById(1));
        System.out.println();

        //Проверка метода updateSubTask, метода getSubTaskById и метода calculateEpicStatus - метода пересчета статуса эпиков
        manager.updateSubTask(subTask4);
        manager.updateSubTask(subTask5);
        manager.updateSubTask(subTask6);
        System.out.println("ТЕСТ 7. Вывод обновленных Подзадач");
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getSubTaskById(6));
        System.out.println(manager.getSubTaskById(7));
        System.out.println();
        System.out.println("ТЕСТ 8. Вывод Эпиков с обновленными статусами:");
        System.out.println(manager.getAllEpics());

        //Проверка метода deleteSubTaskById
        manager.deleteSubTaskById(7);
        System.out.println("ТЕСТ 9. Вывод удаленной Подзадачи");
        System.out.println(manager.getSubTaskById(7));
        System.out.println();
        System.out.println("ТЕСТ 10. Вывод Эпиков с обновленными статусами:");
        System.out.println(manager.getAllEpics());

        //Проверка метода deleteAllSubTasks
        manager.deleteAllSubTasks();
        System.out.println("ТЕСТ 11. Вывод удаленных Подзадач");
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getSubTaskById(6));
        System.out.println(manager.getSubTaskById(7));
        System.out.println();
        System.out.println("ТЕСТ 12. Вывод Эпиков с обновленными статусами:");
        System.out.println(manager.getAllEpics());

        //Проверка метода deleteAllEpics
        manager.deleteAllEpics();
        System.out.println("ТЕСТ 13. Вывод удаленных Эпиков");
        System.out.println(manager.getAllEpics());
        System.out.println();
        System.out.println("ТЕСТ 14. Вывод Подзадач, связанных с удаленными Эпиками:");
        System.out.println(manager.getAllSubTasks());
    }
}


