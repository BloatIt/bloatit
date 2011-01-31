package com.bloatit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.bloatit.common.Log;

public abstract class PlannedTask extends TimerTask implements Serializable {

    private static final long serialVersionUID = 7423363701470187880L;

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

    private static Map<Id, PlannedTask> tasks = new HashMap<Id, PlannedTask>();
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
            Model.lock();
            doRun();
        } catch (final InterruptedException e) {
            Log.model().fatal("Planned task error. ", e);
        } catch (final RuntimeException ex) {
            throw ex;
        } finally {
            remove(this);
            Model.unLock();
        }
    }

    private void remove(final PlannedTask task) {
        tasks.remove(task);
        task.cancel();
    }

    public abstract void doRun();

    // public static void saveTasks(OutputStream os) throws IOException {
    // ObjectOutputStream oos = new ObjectOutputStream(os);
    // oos.writeObject(PlannedTask.tasks);
    // }
    //
    // @SuppressWarnings("unchecked")
    // public static void loadTasks(InputStream is) throws IOException,
    // ClassNotFoundException {
    // ObjectInputStream ois = new ObjectInputStream(is);
    // PlannedTask.tasks = (Map<Id, PlannedTask>) ois.readObject();
    //
    // // Relaunch the timers.
    // for (Entry<Id, PlannedTask> task : PlannedTask.tasks.entrySet()) {
    // timer.schedule(task.getValue(), task.getValue().runDate);
    // }
    // }

}
