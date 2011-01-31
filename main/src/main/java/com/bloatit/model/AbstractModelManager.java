package com.bloatit.model;

public interface AbstractModelManager {

    /**
     * Launch the model layer. This method will be called only once, before everything else.
     */
    public abstract void launch();

    /**
     * Shutdown the model layer. This method will be called only once, at the end of the execution.
     */
    public abstract void shutdown();

    /**
     * Set the current transaction in readOnly mode. You do not have to call this yourself.
     */
    public abstract void setReadOnly();

    /**
     * Open a transaction in normal (rw) mode.
     */
    public abstract void open();

    /**
     * Close the current transaction, tells to the db layer to flush everything.
     */
    public abstract void close();

    /**
     * Reserve the Model layer and make sure nobody else is using it.
     *
     * @throws InterruptedException
     */
    public abstract void lock() throws InterruptedException;

    /**
     * Release the Model layer after we had reserved it.
     */
    public abstract void unLock();

}