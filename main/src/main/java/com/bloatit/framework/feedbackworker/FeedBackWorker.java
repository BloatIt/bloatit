package com.bloatit.framework.feedbackworker;

public abstract class FeedBackWorker <T> {

    private final Class<T> theClass;

    public FeedBackWorker(Class<T> theClass) {
        this.theClass = theClass;
    }
    
    @SuppressWarnings("unchecked")
    public boolean work(Object object){
        if (object.getClass().isAssignableFrom(theClass)){
            return doWork((T) object);
        }
        return false;
    }

    protected abstract boolean doWork(T object);
}
