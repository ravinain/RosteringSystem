/**
 * 
 */
package org.roster.service;

import java.util.List;
import java.util.Map;

import org.roster.model.DayAndShift;
import org.roster.model.Volunteer;

/**
 * Roster Service Layer.
 * @author cdacr
 */
public interface RosterService {
	/**
	 * Fetch the prepared roster.
	 * @param fileName Spreadsheet containing volunteer information.
	 * @return {@link Map} of {@link DayAndShift} and 
	 * {@link List} of {@link Volunteer}.
	 */
	Map<DayAndShift, List<Volunteer>> getRoster(String fileName);
}
