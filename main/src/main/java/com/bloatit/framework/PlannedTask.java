package com.bloatit.framework;

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
        @SuppressWarnings("unused")
        private final int id;
        @SuppressWarnings("unused")
        private final Class<? extends PlannedTask> clazz;

        public Id(int id, Class<? extends PlannedTask> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

    }

    private static Map<Id, PlannedTask> tasks = new HashMap<Id, PlannedTask>();

    private static final Timer timer = new Timer();
    private Date runDate;

    /**
     * An id = 1 planed task.
     *
     * @param time
     * @param id
     */
    public PlannedTask(Date time, int id) {
        super();
        schedule(time);
        PlannedTask.tasks.put(new Id(id, getClass()), this);
    }

    /**
     * @param task
     * @param time
     * @see java.util.Timer#schedule(java.util.TimerTask, java.util.Date)
     */
    public void schedule(Date time) {
        this.runDate = time;
        PlannedTask.timer.schedule(this, time);
    }

    public static boolean updatePlanedTask(Class<? extends PlannedTask> clazz, int id, Date time){
        PlannedTask plannedTask = tasks.get(new Id(id, clazz));
        if (plannedTask != null){
            plannedTask.schedule(time);
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        try {
            Lock.doLock();
            doRun();
        } catch (InterruptedException e) {
            Log.framework().fatal("Planned task error. ", e);
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            remove(this);
            Lock.doUnLock();
        }
    }

    private void remove(PlannedTask task) {
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
