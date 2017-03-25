package org.roster.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Mapping of Shift and Volunteer.
 * @author cdacr
 */
public final class ShiftVolunteerMapping extends
		HashMap<String, List<Volunteer>> {

	/** */
	private static final long serialVersionUID = 7948614826060732432L;

	/**
	 * Add new entry in {@link ShiftVolunteerMapping}.
	 * @param key Shift
	 * @param volunteer {@link Volunteer}
	 */
	public void addEntry(final String key, final Volunteer volunteer) {
		List<Volunteer> volunteers;
		if (this.containsKey(key)) {
			volunteers = this.get(key);
		} else {
			volunteers = new ArrayList<>();
		}
		if (!volunteers.contains(volunteer)) {
			volunteers.add(volunteer);
		}
		this.put(key, volunteers);
	}
}
