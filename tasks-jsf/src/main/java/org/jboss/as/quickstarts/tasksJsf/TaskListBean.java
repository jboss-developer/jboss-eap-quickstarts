package org.jboss.as.quickstarts.tasksJsf;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;


/**
 * <p>
 * Operations with cached list of tasks for current user.
 * </p>
 *
 * <p>
 * Delegates to {@link TaskDao} for persistence operations.
 * </p>
 *
 * <p>
 * This bean ensures that task list will be obtained at most once per request or additionally after each invalidation (
 * {@link #invalidate()}).
 * </p>
 *
 * <p>
 * This behavior prevents unnecessary delegations to the persistence layer, since {{@link #getAll()} can be called several times
 * per request when used in view layer.
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
