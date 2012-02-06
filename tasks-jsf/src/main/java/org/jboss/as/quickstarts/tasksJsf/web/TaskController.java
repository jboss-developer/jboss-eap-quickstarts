package org.jboss.as.quickstarts.tasksJsf.web;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.tasksJsf.beans.Repository;
import org.jboss.as.quickstarts.tasksJsf.domain.Task;
import org.jboss.as.quickstarts.tasksJsf.domain.TaskDao;
import org.jboss.as.quickstarts.tasksJsf.domain.User;
import org.jboss.as.quickstarts.tasksJsf.qualifiers.CurrentTask;
import org.jboss.as.quickstarts.tasksJsf.qualifiers.CurrentUser;

/**
 * <p>
 * Basic operations for tasks owned by current user - additions, deletions - and provides current selected task.
 * </p>
 *
 * @author Lukas Fryc
 *
 */
@Named("tasks")
@ConversationScoped
public class TaskController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    TaskDao taskDao;

    @Inject
    TaskList taskList;

    @Inject
    Repository repository;

    /**
     * Injects current user, which provided by {@link AuthController}.
     */
    @Inject
    @CurrentUser
    User currentUser;

    private Task currentTask;

    /**
     * <p>
     * Provides current task to the context available for injection using:
     * </p>
     *
     * <p>
     * <code>@Inject @CurrentTask currentTask;</code>
     * </p>
     *
     * <p>
     * or from the Expression Language context using an expression <code>#{currentTask}</code>.
     * </p>
     *
     * @return current authenticated user
     */
    @Produces
    @CurrentTask
    @Named
    public Task getCurrentTask() {
        return currentTask;
    }

    /**
     * Setup current task
     *
     * @param currentTask task to setup as current
     */
    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    /**
     * Creates new task and, if no task is selected as current, selects it.
     *
     * @param taskTitle
     */
    public void createTask(String taskTitle) {
        Task task = new Task(taskTitle);
        taskList.invalidate();
        taskDao.createTask(currentUser, task);
        if (currentTask == null) {
            currentTask = task;
        }
    }

    /**
     * Deletes given task
     *
     * @param task to delete
     */
    public void deleteTask(Task task) {
        if (task.equals(currentTask)) {
            currentTask = null;
        }
        taskList.invalidate();
        taskDao.deleteTask(task);
    }
}
