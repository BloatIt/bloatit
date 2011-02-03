package com.bloatit.framework.webserver;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.model.AbstractModel;

public class ModelManagerAccessor {

    private static AbstractModel modelManager = null;

    private static void setModelManager(AbstractModel manager) {
        if (modelManager == null) {
            modelManager = manager;
        } else {
            throw new FatalErrorException("You can set the modelManager only once !");
        }
    }

    /**
     * @see com.bloatit.model.AbstractModel#launch()
     */
    public static void launch(AbstractModel manager) {
        setModelManager(manager);
        modelManager.launch();
    }

    /**
     * @see com.bloatit.model.AbstractModel#shutdown()
     */
    public static void shutdown() {
        modelManager.shutdown();
    }

    /**
     * @see com.bloatit.model.AbstractModel#setReadOnly()
     */
    public static void setReadOnly() {
        modelManager.setReadOnly();
    }

    /**
     * @see com.bloatit.model.AbstractModel#open()
     */
    public static void open() {
        modelManager.open();
    }

    /**
     * @see com.bloatit.model.AbstractModel#close()
     */
    public static void close() {
        modelManager.close();
    }
}
