package com.bloatit.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public abstract class PlannedTask extends TimerTask implements Serializable {

    private static final long serialVersionUID = 7423363701470187880L;

    private static Set<PlannedTask> tasks = new HashSet<PlannedTask>();

    private final Timer timer;
    private final Date runDate;

    public PlannedTask(Date time) {
        super();
        this.timer = new Timer();
        this.runDate = time;
        PlannedTask.tasks.add(this);
    }

    @Override
    public void run() {
        doRun();
        tasks.remove(this);
    }

    public abstract void doRun();

    // /**
    // * @see java.util.Timer#schedule(java.util.TimerTask, long)
    // */
    // public void schedule(long delay) {
    // timer.schedule(this, delay);
    // }
    //
    // /**
    // * @see java.util.Timer#schedule(java.util.TimerTask, java.util.Date, long)
    // */
    // public void schedule(Date firstTime, long period) {
    // timer.schedule(this, firstTime, period);
    // }

    /**
     * @see java.util.Timer#schedule(java.util.TimerTask, long, long)
     */
    public void schedule(long delay, long period) {
        timer.schedule(this, delay, period);
    }

    public static void saveTasks(OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(PlannedTask.tasks);
    }

    @SuppressWarnings("unchecked")
    public static void loadTasks(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        PlannedTask.tasks = (Set<PlannedTask>) ois.readObject();

        // Relaunch the timers.
        for (PlannedTask task : PlannedTask.tasks) {
            task.timer.schedule(task, task.runDate);
        }
    }

}
