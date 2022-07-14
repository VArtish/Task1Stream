package service;

import entity.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface TaskService {
    void fillList(List<Task> tasks, int quantity);

    List<Task> fetchAndSortTask(List<Task> tasks, Task.TaskType type);

    Optional<Task> findTaskByCondition(List<Task> tasks, Predicate<Task> predicate);

    Optional<LocalDate> findDayByCondition(List<Task> tasks, Predicate<Task> predicate);

    Map<Task.TaskType, Long> groupTaskTypeByQuantity(List<Task> tasks);

    boolean convertTitle(String separator, List<Task> tasks, String path);
}
