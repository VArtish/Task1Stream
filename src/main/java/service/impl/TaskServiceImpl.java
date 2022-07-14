package service.impl;

import entity.Task;
import lombok.extern.log4j.Log4j2;
import service.TaskService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {
    private static final String TASK = "Task ";
    private static final Integer UPPER_BOUND = 3;
    private static final Integer CURRENT_YEAR = 2022;
    private static final Integer DAY_IN_YEAR = 365;
    private static TaskServiceImpl instance;

    private TaskServiceImpl() {
    }

    public static TaskServiceImpl getInstance() {
        if (instance == null) {
            instance = new TaskServiceImpl();
        }

        return instance;
    }

    @Override
    public void fillList(List<Task> tasks, int quantity) {
        Task.TaskType[] types = Task.TaskType.values();
        for (int i = 0; i < quantity; i++) {
            long id = i;
            int randomNumber = (int) (Math.random() * UPPER_BOUND);
            Task.TaskType type = types[randomNumber];
            String title = TASK + i;
            int randomDay = generateRandomDay();
            Task task = new Task(id, title, type, LocalDate.ofYearDay(CURRENT_YEAR, randomDay));
            tasks.add(task);
        }
    }

    @Override
    public List<Task> fetchAndSortTask(List<Task> tasks, Task.TaskType type) {
        List<Task> modifiedList = tasks.stream()
                .filter(task -> task.getType() == type)
                .sorted(Comparator.comparing(item1 -> item1.getCreated()))
                .toList();

        return modifiedList;
    }

    @Override
    public Optional<Task> findTaskByCondition(List<Task> tasks, Predicate<Task> predicate) {
        Optional<Task> task = tasks.stream()
                .filter(predicate)
                .findFirst();

        return task;
    }

    @Override
    public Optional<LocalDate> findDayByCondition(List<Task> tasks, Predicate<Task> predicate) {
        if(tasks.isEmpty()) {
            return Optional.empty();
        }

        Map<LocalDate, Long> modifiedMap = tasks.stream()
                .collect(Collectors.groupingBy(Task::getCreated,
                        Collectors.counting()));
        Optional<LocalDate> localDate = modifiedMap.keySet().stream().min(Comparator.naturalOrder());


        return localDate;
    }

    @Override
    public Map<Task.TaskType, Long> groupTaskTypeByQuantity(List<Task> tasks) {
        Map<Task.TaskType, Long> resultMap = tasks.stream()
                .collect(Collectors.groupingBy(Task::getType, Collectors.counting()));

        return resultMap;
    }

    @Override
    public boolean convertTitle(String separator, List<Task> tasks, String path) {
        String result = tasks.stream()
                .map(task -> task.getTitle()).collect(Collectors.joining(separator));

        if (!Files.exists(Paths.get(path))) {
            return false;
        }

        try (FileWriter writer = new FileWriter(path, false)) {
            writer.write(result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return true;
    }

    private int generateRandomDay() {
        int randomNumber = (int) (Math.random() * DAY_IN_YEAR);
        return randomNumber == 0 ? 1 : randomNumber;
    }
}
