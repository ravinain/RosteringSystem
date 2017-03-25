package org.roster.util.ut;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.roster.model.DayAndShift;
import org.roster.model.DayShiftMapping;
import org.roster.model.ShiftVolunteerMapping;
import org.roster.model.Volunteer;

/**
 * {@link DayShiftMapping} Test Class.
 * @author cdacr
 *
 */
public class DayShiftMappingTest {

	/** */
	private DayShiftMapping dayShiftMapping;

	/**
	 * create new object for every test case.
	 */
	@Before
	public void setUp() {
		dayShiftMapping = new DayShiftMapping();
	}

	/**
	 * release the object after completion of test case.
	 */
	@After
	public void tearDown() {
		dayShiftMapping = null;
	}

	/**
	 * Test add day shift call.
	 */
	@Test
	public void testAddDayShift() {
		final Volunteer andrew = new Volunteer();
		andrew.setName("Andrew");
		andrew.addAvailability(DayAndShift.MONDAY_AFTERNOON);
		andrew.addAvailability(DayAndShift.TUESDAY_MORNING);
		andrew.addAvailability(DayAndShift.WEDNESDAY_EITHER);

		dayShiftMapping.addDayShift(andrew);

		final Map<String, ShiftVolunteerMapping> dayShiftMap = dayShiftMapping
				.getDayShiftMap();
		System.out.println(dayShiftMap);
		assertNotNull("Day shift mapping must not null!", dayShiftMap);
		assertThat(dayShiftMap.size(), is(3));
	}

	/**
	 * Test Sorted map by volunteer count call.
	 */
	@Test
	public void testGetSortedMapByVolunteerCount() {
		final Volunteer andrew = new Volunteer();
		andrew.setName("Andrew");
		andrew.addAvailability(DayAndShift.MONDAY_AFTERNOON);
		andrew.addAvailability(DayAndShift.TUESDAY_MORNING);
		andrew.addAvailability(DayAndShift.WEDNESDAY_EITHER);

		dayShiftMapping.addDayShift(andrew);

		final Volunteer raj = new Volunteer();
		raj.setName("Raj");
		raj.addAvailability(DayAndShift.MONDAY_EITHER);
		raj.addAvailability(DayAndShift.WEDNESDAY_AFTERNOON);

		dayShiftMapping.addDayShift(raj);

		final Map<String, ShiftVolunteerMapping> sortedMap = dayShiftMapping
				.getSortedMapByVolunteerCount();
		System.out.println(sortedMap);
		assertNotNull(sortedMap);
		final Set<String> keys = sortedMap.keySet();
		assertNotNull(keys);
		for (final String key : keys) {
			assertThat(key, equalTo(DayAndShift.TUESDAY_MORNING.getDay()));
			break;
		}
	}
}
