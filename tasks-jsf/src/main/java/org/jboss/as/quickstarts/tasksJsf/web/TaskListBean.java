package org.jboss.as.quickstarts.tasksJsf.web;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.tasksJsf.domain.Task;
import org.jboss.as.quickstarts.tasksJsf.domain.TaskDao;
import org.jboss.as.quickstarts.tasksJsf.domain.User;
import org.jboss.as.quickstarts.tasksJsf.qualifiers.CurrentUser;

/**
 * <p>
 * Operations with cached list of tasks for current user.
 * </p>
 *
 * <p>
 * Delegates to {@link TaskDao} for persistence operations.
 * </p>
 *
 * @author Lukas Fryc
 */
@Named("taskList")
@RequestScoped
public class TaskListBean implements TaskList, Serializable {

    private static final long serialVersionUID = 6500945303939594703L;

    private List<Task> tasks;

    @Inject
    TaskDao taskDao;

    @Inject
    @CurrentUser
    User currentUser;

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.as.quickstarts.tasksJsf.web.TaskList#getAll()
     */
    @Override
    public List<Task> getAll() {
        if (tasks == null) {
            tasks = taskDao.getAll(currentUser);
        }
        return tasks;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.as.quickstarts.tasksJsf.web.TaskList#invalidate()
     */
    @Override
    public void invalidate() {
        tasks = null;
    }
}
