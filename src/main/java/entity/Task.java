package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Task {
    private final Long id;
    private final String title;
    private final TaskType type;
    private final LocalDate created;

    public enum TaskType {
        NEW,
        PROCESSING,
        FINISHED
    }
}