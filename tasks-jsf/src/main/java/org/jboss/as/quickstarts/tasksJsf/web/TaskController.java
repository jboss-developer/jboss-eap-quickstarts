package org.jboss.as.quickstarts.tasksJsf.web;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.tasksJsf.beans.CurrentTaskStore;
import org.jboss.as.quickstarts.tasksJsf.domain.Task;
import org.jboss.as.quickstarts.tasksJsf.domain.TaskDao;
import org.jboss.as.quickstarts.tasksJsf.domain.User;
import org.jboss.as.quickstarts.tasksJsf.qualifiers.CurrentUser;

/**
 * <p>
 * Basic operations for tasks owned by current user - additions, deletions/
 * </p>
 * 
 * @author Lukas Fryc
 * 
 */
@RequestScoped
@Named("tasks")
public class TaskController {

    @Inject
    TaskDao taskDao;

    @Inject
    TaskList taskList;

    /**
     * Injects current user, which provided by {@link AuthController}.
     */
    @Inject
    @CurrentUser
    Instance<User> currentUser;

    @Inject
    CurrentTaskStore currentTaskStore;

    /**
     * Creates new task and, if no task is selected as current, selects it.
     * 
     * @param taskTitle
     */
    public void createTask(String taskTitle) {
        taskList.invalidate();
        Task task = new Task(taskTitle);
        taskDao.createTask(currentUser.get(), task);
        if (currentTaskStore.get() == null) {
            currentTaskStore.set(task);
        }
    }

    /**
     * Deletes given task
     * 
     * @param task to delete
     */
    public void deleteTask(Task task) {
        taskList.invalidate();
        if (task.equals(currentTaskStore.get())) {
            currentTaskStore.unset();
        }
        taskDao.deleteTask(task);
    }

    /**
     * Deletes given task
     * 
     * @param task to delete
     */
    public void deleteCurrentTask() {
        deleteTask(currentTaskStore.get());
    }
}
