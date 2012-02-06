package org.jboss.as.quickstarts.tasksJsf.domain;

import java.util.List;

/**
 * Basic operations for manipulation with tasks
 *
 * @author Lukas Fryc
 *
 */
public interface TaskDao {

    void createTask(User user, Task task);

    List<Task> getAll(User user);

    List<Task> getRange(User user, int offset, int count);

    List<Task> getForTitle(User user, String title);

    void deleteTask(Task task);
}
