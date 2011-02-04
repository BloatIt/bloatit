package com.bloatit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.bloatit.framework.webserver.ModelManagerAccessor;

/**
 * <p>
 * Extends this task, implement the doRun method, and you have a custom planned task to
 * run when you want.
 * </p>
 * <p>
 * This class is thread safe. If you only implement the doRun method, there should be no
 * thread safety problem with your sub classes.
 * </p>
 */
public abstract class PlannedTask extends TimerTask implements Serializable {

    private static final long serialVersionUID = 7423363701470187880L;

    private static final ConcurrentMap<Id, PlannedTask> tasks = new ConcurrentHashMap<Id, PlannedTask>();
    /**
     * The timer class is thread safe.
     */
    private static final Timer timer = new Timer();

    /**
     * An id = 1 planed task.
     *
     * @param time
     * @param id
     */
    public PlannedTask(final Date time, final int id) {
        super();
        schedule(time);
        final PlannedTask plannedTask = PlannedTask.tasks.get(new Id(id, getClass()));
        if (plannedTask != null) {
            plannedTask.cancel();
        }
        PlannedTask.tasks.put(new Id(id, getClass()), this);
    }

    /**
     * @param task
     * @param time
     * @see java.util.Timer#schedule(java.util.TimerTask, java.util.Date)
     */
    public void schedule(final Date time) {
        PlannedTask.timer.schedule(this, time);
    }

    @Override
    public void run() {
        try {
            ModelManagerAccessor.open();
            doRun();
        } catch (final RuntimeException ex) {
            throw ex;
        } finally {
            remove(this);
            ModelManagerAccessor.close();
        }
    }

    private void remove(final PlannedTask task) {
        tasks.remove(task);
        task.cancel();
    }

    public abstract void doRun();

    /**
     * Immutable class. It is an Id, use it to identify a plannedTask.
     */
    private static final class Id implements Serializable {
        private static final long serialVersionUID = -6892244222686715273L;
        private final int id;
        private final Class<? extends PlannedTask> clazz;

        public Id(final int id, final Class<? extends PlannedTask> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
            result = prime * result + id;
            return result;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Id other = (Id) obj;
            if (clazz == null) {
                if (other.clazz != null) {
                    return false;
                }
            } else if (!clazz.equals(other.clazz)) {
                return false;
            }
            if (id != other.id) {
                return false;
            }
            return true;
        }
    }
}
