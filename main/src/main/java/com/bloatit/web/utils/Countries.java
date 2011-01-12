package com.bloatit.web.utils;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.bloatit.common.FatalErrorException;

/**
 * Class to handle tools related to countries managements.
 */
public class Countries {

	private static final String COUNTRIES_PATH = "i18n/countries";
	private static Set<Country> availableCountries = null;

	/**
	 * <p>Lists all available countries ordered on their fullname</p>
	 * @return a list of the available countries
	 */
	public static Set<Country> getAvailableCountries(){
		if(availableCountries == null){
			availableCountries = new TreeSet<Country>();
			initAvailableCountries();
		}
		return availableCountries;
	}



	private static void initAvailableCountries(){
		try {
			Properties properties = PropertyLoader.loadProperties(COUNTRIES_PATH);
			for(Entry<?, ?> property : properties.entrySet()){
				String key = (String)property.getKey();
				String value = (String)property.getValue();
				availableCountries.add(new Country(value, key));
			}
		} catch (IOException e) {
			throw new FatalErrorException("File describing available countries is not available at "+COUNTRIES_PATH, e);
		}
	}

	/**
	 * Nested class to represent a country
	 */
	public static class Country implements Comparable<Country>{
		public String name;
		public String code;

		public Country(String name, String code) {
			super();
			this.name = name;
			this.code = code;
		}

		@Override
		public int compareTo(Country o) {
			return this.name.compareTo(o.name);
		}

		@Override
		public int hashCode() {
		    assert false : "hashCode not designed";
		    return 42; // any arbitrary constant will do
		  }

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Country other = (Country) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}



	}
}
