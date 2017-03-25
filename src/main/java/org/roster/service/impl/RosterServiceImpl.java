/**
 * 
 */
package org.roster.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.roster.dao.RosterDao;
import org.roster.dao.impl.RosterDaoImpl;
import org.roster.model.DayAndShift;
import org.roster.model.DayShiftMapping;
import org.roster.model.Shift;
import org.roster.model.ShiftVolunteerMapping;
import org.roster.model.Volunteer;
import org.roster.service.RosterService;

/**
 * @author cdacr
 *
 */
public final class RosterServiceImpl implements RosterService {

	/** RosterDao. */
	private final RosterDao rosterDao = new RosterDaoImpl();

	/** 
	 * @see RosterService#getRoster(String)
	 */
	@Override
	public Map<DayAndShift, List<Volunteer>> getRoster(final String fileName) {
		Map<DayAndShift, List<Volunteer>> rosterMap = null;
		try {
			final List<Volunteer> volunteers = rosterDao
					.getVolunteerInfo(fileName);
			rosterMap = prepareRoster(volunteers);
		} catch (final IOException ex) {
			System.err.println(ex.getMessage());
		}

		return rosterMap;
	}

	/**
	 * Prepare roster.
	 * @param volunteers {@link List} of {@link Volunteer}
	 * @return Map<DayAndShift, List<Volunteer>>
	 */
	private Map<DayAndShift, List<Volunteer>> prepareRoster(
			final List<Volunteer> volunteers) {
		final DayShiftMapping dayShiftMapping = new DayShiftMapping();

		// prepare day shift mapping.
		for (final Volunteer volunteer : volunteers) {
			dayShiftMapping.addDayShift(volunteer);
		}

		return processData(dayShiftMapping, volunteers);
	}

	/**
	 * Process DayShiftMapping data.
	 * @param dayShiftMapping {@link DayShiftMapping}
	 * @param availabilities {@link List} of {@link Volunteer} 
	 * with availabilities.
	 * @return Map<DayAndShift, List<Volunteer>>
	 */
	private Map<DayAndShift, List<Volunteer>> processData(
			final DayShiftMapping dayShiftMapping,
			final List<Volunteer> availabilities) {
		final Map<DayAndShift, List<Volunteer>> rosterMap = new HashMap<>();
		final Map<Volunteer, Integer> shiftCountMap = new HashMap<>();
		final Map<String, ShiftVolunteerMapping> sortedDayShiftMap = dayShiftMapping
				.getSortedMapByVolunteerCount();
		final Set<String> sortedDayShiftKeys = sortedDayShiftMap.keySet();

		setDeafultShiftCountMapData(shiftCountMap, availabilities);

		for (final String day : sortedDayShiftKeys) {
			final ShiftVolunteerMapping shiftMap = sortedDayShiftMap.get(day);
			processShiftData(shiftMap, day, Shift.AFTERNOON.name(), rosterMap,
					shiftCountMap);
			processShiftData(shiftMap, day, Shift.MORNING.name(), rosterMap,
					shiftCountMap);
			processShiftData(shiftMap, day, Shift.EITHER.name(), rosterMap,
					shiftCountMap);
		}

		processUnallocatedVolunteers(shiftCountMap, rosterMap);

		return rosterMap;
	}

	/**
	 * Set default shift count of all volunteers to zero.
	 * @param shiftCountMap ShiftCountMap.
	 * @param availabilities {@link List} of {@link Volunteer} 
	 * with their availabilities.
	 */
	private void setDeafultShiftCountMapData(
			final Map<Volunteer, Integer> shiftCountMap,
			final List<Volunteer> availabilities) {
		// fill default data in map
		for (final Volunteer volunteer : availabilities) {
			shiftCountMap.put(volunteer, 0);
		}
	}

	/**
	 * Process the shift data and allocate the volunteers.
	 * @param shiftMap {@link ShiftVolunteerMapping}
	 * @param currentDay value of day such as MONDAY|TUESDAY.
	 * @param currentShift value of shift such as AFTERNOON|MORNING
	 * @param rosterMap Map contains roster information. 
	 * This will be updated with allocation information.
	 * @param shiftCountMap Contains volunteers' shift count.
	 */
	private void processShiftData(final ShiftVolunteerMapping shiftMap,
			final String currentDay, final String currentShift,
			final Map<DayAndShift, List<Volunteer>> rosterMap,
			final Map<Volunteer, Integer> shiftCountMap) {
		if (shiftMap.containsKey(currentShift)
				&& !currentShift.equals(Shift.EITHER.name())) {
			final List<Volunteer> currentVolunteers = shiftMap
					.get(currentShift);
			if (currentVolunteers != null) {
				final int size = currentVolunteers.size();
				final List<Volunteer> scheduledVolunteers = new ArrayList<>();
				for (int index = 0; index < (size < 2 ? size : 2); index++) {
					final Volunteer cVolunteer = currentVolunteers.get(index);
					final int count = shiftCountMap.get(cVolunteer) + 1;
					if (count <= 3) {
						shiftCountMap.put(cVolunteer, count);
						scheduledVolunteers.add(cVolunteer);
						cVolunteer.getAvailabilities().remove(
								DayAndShift.valueOf(currentDay + "_"
										+ currentShift));
					}
				}

				// clear current volunteers
				currentVolunteers.removeAll(scheduledVolunteers);
				rosterMap.put(
						DayAndShift.valueOf(currentDay + "_" + currentShift),
						scheduledVolunteers);
			}
		} else if (currentShift.equals(Shift.EITHER.name())
				&& shiftMap.containsKey(currentShift)) {
			final List<Volunteer> currentVolunteers = shiftMap
					.get(currentShift);
			List<Volunteer> afternoonVolunteers = rosterMap.get(DayAndShift
					.valueOf(currentDay + "_" + Shift.AFTERNOON.name()));
			if (afternoonVolunteers == null || afternoonVolunteers.size() < 2) {
				if (afternoonVolunteers == null) {
					rosterMap.put(
							DayAndShift.valueOf(currentDay + "_"
									+ Shift.AFTERNOON.name()),
							new ArrayList<Volunteer>());
					afternoonVolunteers = rosterMap
							.get(DayAndShift.valueOf(currentDay + "_"
									+ Shift.AFTERNOON.name()));
				}
				final int size = afternoonVolunteers == null ? 0
						: afternoonVolunteers.size();
				for (int index = 0, counter = size; index < currentVolunteers
						.size() && counter < 2; index++, counter++) {
					final Volunteer cVolunteer = currentVolunteers.get(index);
					final int count = shiftCountMap.get(cVolunteer) + 1;
					if (count <= 3) {
						shiftCountMap.put(cVolunteer, count);
						afternoonVolunteers.add(cVolunteer);
						cVolunteer.getAvailabilities().remove(
								DayAndShift.valueOf(currentDay + "_"
										+ currentShift));
					}
				}
				if (afternoonVolunteers != null) {
					// Remove newly added volunteer from current volunteers.
					currentVolunteers.removeAll(afternoonVolunteers);
				}
			}

			List<Volunteer> morningVolunteers = rosterMap.get(DayAndShift
					.valueOf(currentDay + "_" + Shift.MORNING.name()));

			if (morningVolunteers == null || morningVolunteers.size() < 2) {
				if (morningVolunteers == null) {
					rosterMap.put(
							DayAndShift.valueOf(currentDay + "_"
									+ Shift.MORNING.name()),
							new ArrayList<Volunteer>());
					morningVolunteers = rosterMap.get(DayAndShift
							.valueOf(currentDay + "_" + Shift.MORNING.name()));
				}
				final int size = morningVolunteers == null ? 0
						: morningVolunteers.size();
				for (int index = 0, counter = size; index < currentVolunteers
						.size() && counter < 2; index++, counter++) {
					final Volunteer cVolunteer = currentVolunteers.get(index);
					final int count = shiftCountMap.get(cVolunteer) + 1;
					if (count <= 3) {
						shiftCountMap.put(cVolunteer, count);
						morningVolunteers.add(cVolunteer);
						cVolunteer.getAvailabilities().remove(
								DayAndShift.valueOf(currentDay + "_"
										+ currentShift));
					}
				}
				// Remove newly added volunteer from current volunteers.
				if (morningVolunteers != null) {
					currentVolunteers.removeAll(morningVolunteers);
				}
			}
		}
	}

	/**
	 * Process the available unallocated volunteers.
	 * @param rosterMap Map contains roster information. 
	 * This will be updated with allocation information.
	 * @param shiftCountMap Contains volunteers' shift count.
	 */
	private void processUnallocatedVolunteers(
			final Map<Volunteer, Integer> shiftCountMap,
			final Map<DayAndShift, List<Volunteer>> rosterMap) {
		final Set<Volunteer> volunteers = getSortedShiftCount(shiftCountMap)
				.keySet();

		for (final Volunteer volunteer : volunteers) {
			final int count = shiftCountMap.get(volunteer);
			if (count < 3) {
				final List<DayAndShift> availabilities = volunteer
						.getAvailabilities();
				final int availableSize = availabilities.size();
				final List<DayAndShift> removeAvaiShifts = new ArrayList<>();
				for (int counter = count, index = 0; counter < (count == 0 ? 3
						: 4) && index < availableSize; counter++, index++) {
					if (availabilities.get(index).getShift()
							.equals(Shift.EITHER.name())) {
						final String key = availabilities.get(index).getDay();
						final DayAndShift dayKey = DayAndShift.valueOf(key
								+ "_" + Shift.AFTERNOON.name());
						if (!rosterMap.containsKey(dayKey)) {
							rosterMap.put(dayKey, new ArrayList<Volunteer>());
						}
						rosterMap.get(dayKey).add(volunteer);
					} else {
						if (!rosterMap.containsKey(availabilities.get(index))) {
							rosterMap.put(availabilities.get(index),
									new ArrayList<Volunteer>());
						}
						rosterMap.get(availabilities.get(index)).add(volunteer);
					}
					removeAvaiShifts.add(availabilities.get(index));
				}
				if (removeAvaiShifts != null) {
					availabilities.removeAll(removeAvaiShifts);
				}
			}
		}
	}

	/**
	 * Comparator to sort the ShiftCount map by its values in ascending order.
	 * @author cdacr
	 */
	class ShiftCountSortByValueAsc implements
			Comparator<Map.Entry<Volunteer, Integer>> {

		@Override
		public int compare(final Entry<Volunteer, Integer> o1,
				final Entry<Volunteer, Integer> o2) {
			return o1.getValue() - o2.getValue();
		}

	}

	/**
	 * Fetch the sorted shift count map.
	 * @param shiftCountMap ShiftCountMap
	 * @return Sorted ShiftCount map.
	 */
	private Map<Volunteer, Integer> getSortedShiftCount(
			final Map<Volunteer, Integer> shiftCountMap) {
		final List<Map.Entry<Volunteer, Integer>> shiftCountList = new LinkedList<>(
				shiftCountMap.entrySet());
		Collections.sort(shiftCountList, new ShiftCountSortByValueAsc());

		final Map<Volunteer, Integer> sortedShiftCount = new LinkedHashMap<>();
		for (final Map.Entry<Volunteer, Integer> entry : shiftCountList) {
			sortedShiftCount.put(entry.getKey(), entry.getValue());
		}

		return sortedShiftCount;
	}
}
