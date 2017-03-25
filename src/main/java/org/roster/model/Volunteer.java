package org.roster.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Volunteer model class.
 * @author cdacr
 */
public class Volunteer {
	/** volunteer name. */
	private String name;
	/** volunteer availabilities. */
	private final List<DayAndShift> availabilities = new ArrayList<>();

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the availabilities
	 */
	public final List<DayAndShift> getAvailabilities() {
		return availabilities;
	}

	/**
	 * @param availabilities the availabilities to set
	 */
	public final void addAvailability(final DayAndShift dayAndShift) {
		this.availabilities.add(dayAndShift);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Volunteer) {
			final Volunteer volunteer = (Volunteer) obj;
			if (volunteer.getName() == null && this.getName() == null) {
				return true;
			}
			if ((volunteer.getName() == null && this.getName() != null)
					|| (volunteer.getName() != null && this.getName() == null)) {
				return false;
			}
			return volunteer.getName().equals(this.getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashcode = 31 * 7;
		if (this.name != null) {
			hashcode += this.name.hashCode();
		}
		return hashcode;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
