package org.roster.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * DayShiftMapping.java.
 * It contains the mapping of Days with all available shifts. 
 * Again shift has all available volunteer list.
 * @author cdacr
 */
public final class DayShiftMapping {

	/** It has mapping of Day and All available shifts. */
	private final Map<String, ShiftVolunteerMapping> dayShiftMap = new HashMap<>();

	/**
	 * Add entry in dayShiftMap based on volunteer availability.
	 * @param volunteer {@link Volunteer}
	 */
	public void addDayShift(final Volunteer volunteer) {
		final List<DayAndShift> dayAndShifts = volunteer.getAvailabilities();
		for (final DayAndShift dayAndShift : dayAndShifts) {
			final String day = dayAndShift.getDay();
			final String shift = dayAndShift.getShift();
			ShiftVolunteerMapping shiftVolMap = dayShiftMap.get(day);
			// create new ShiftVolunteerMapping if it does not exists in the
			// dayShiftMap.
			if (!dayShiftMap.containsKey(day)) {
				shiftVolMap = new ShiftVolunteerMapping();
			}
			shiftVolMap.addEntry(shift, volunteer);
			dayShiftMap.put(day, shiftVolMap);
		}
	}

	/**
	 * Fetch the sorted map by volunteer availability count on per day.
	 * @see SortByVolunteerCount 
	 * @return Sorted map based on {@link SortByVolunteerCount}.
	 */
	public Map<String, ShiftVolunteerMapping> getSortedMapByVolunteerCount() {
		final List<Map.Entry<String, ShiftVolunteerMapping>> dayShifts = new LinkedList<>(
				dayShiftMap.entrySet());
		Collections.sort(dayShifts, new SortByVolunteerCount());
		final Map<String, ShiftVolunteerMapping> sortedDayShifts = new LinkedHashMap<>();
		for (final Map.Entry<String, ShiftVolunteerMapping> entry : dayShifts) {
			sortedDayShifts.put(entry.getKey(), entry.getValue());
		}
		return sortedDayShifts;
	}

	/**
	 * Sort the mapping of Day and its associated {@link ShiftVolunteerMapping}
	 * based on the availability of volunteers for particular shift.
	 * For eg: Availability on Monday Morning(2), Afternoon(1) and Either(3), 
	 * on Tuesday Morning(2), Afternoon(2) and Either(2)
	 * The sum of Monday's availability is 6 whereas Tuesday's is 6.
	 * Check the difference between their sum as well as check if anyone 
	 * has Morning and Afternoon availability less than 1. 
	 * If so, then less value object will display first.
	 * Thus, Monday will be added before Tuesday as Afternoon has 1 value 
	 * which is less than 2.  
	 * @author cdacr
	 *
	 */
	private class SortByVolunteerCount implements
			Comparator<Map.Entry<String, ShiftVolunteerMapping>> {

		@Override
		public int compare(final Entry<String, ShiftVolunteerMapping> o1,
				final Entry<String, ShiftVolunteerMapping> o2) {
			final ShiftVolunteerMapping shift1 = o1.getValue();
			final ShiftVolunteerMapping shift2 = o2.getValue();

			if (shift1 == null && shift2 == null) {
				return 0;
			}
			if (shift1 == null && shift2 != null) {
				return -1;
			}
			if (shift1 != null && shift2 == null) {
				return 1;
			}
			return compareShiftCount(shift1, shift2);
		}

		/**
		 * Compare the values based on logic specified above.
		 * @param shift1 {@link ShiftVolunteerMapping}.
		 * @param shift2 {@link ShiftVolunteerMapping}.
		 * @return -1|0|1
		 */
		private int compareShiftCount(final ShiftVolunteerMapping shift1,
				final ShiftVolunteerMapping shift2) {
			final List<Volunteer> morgAvailabilities1 = shift1
					.get(Shift.MORNING.name());
			final List<Volunteer> aftAvailabilities1 = shift1
					.get(Shift.AFTERNOON.name());
			final List<Volunteer> eithAvailabilities1 = shift1.get(Shift.EITHER
					.name());

			final List<Volunteer> morgAvailabilities2 = shift2
					.get(Shift.MORNING.name());
			final List<Volunteer> aftAvailabilities2 = shift2
					.get(Shift.AFTERNOON.name());
			final List<Volunteer> eithAvailabilities2 = shift2.get(Shift.EITHER
					.name());

			final int mrngSize1 = morgAvailabilities1 == null ? 0
					: morgAvailabilities1.size();
			final int aftSize1 = aftAvailabilities1 == null ? 0
					: aftAvailabilities1.size();
			final int eithSize1 = eithAvailabilities1 == null ? 0
					: eithAvailabilities1.size();

			final int mrngSize2 = morgAvailabilities2 == null ? 0
					: morgAvailabilities2.size();
			final int aftSize2 = aftAvailabilities2 == null ? 0
					: aftAvailabilities2.size();
			final int eithSize2 = eithAvailabilities2 == null ? 0
					: eithAvailabilities2.size();

			int retValue = (mrngSize1 + aftSize1 + eithSize1)
					- (mrngSize2 + aftSize2 + eithSize2);

			if (mrngSize1 > 1 && aftSize1 > 1
					&& (mrngSize2 < 2 || aftSize2 < 2)) {
				retValue = 1;
			} else if (mrngSize2 > 1 && aftSize2 > 1
					&& (mrngSize1 < 2 || aftSize1 < 2)) {
				retValue = -1;
			}
			return retValue;
		}
	}

	/**
	 * Fetch dayShiftMap.
	 * @return {@link Map}
	 */
	public Map<String, ShiftVolunteerMapping> getDayShiftMap() {
		return this.dayShiftMap;
	}
}
