package com.bloatit.model.data.util;

public class DBRequests {

	@SuppressWarnings("unchecked")
	public static <T> T getById(Class<T> persistant, Integer id) {
		return (T) SessionManger.getSessionFactory().getCurrentSession()
				.get(persistant, id);
	}

}
