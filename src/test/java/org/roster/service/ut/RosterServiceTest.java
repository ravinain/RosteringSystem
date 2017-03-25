package org.roster.service.ut;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.roster.dao.RosterDao;
import org.roster.model.DayAndShift;
import org.roster.model.Volunteer;
import org.roster.service.RosterService;
import org.roster.service.impl.RosterServiceImpl;
import org.roster.util.Constants;

/**
 * Test class for {@link RosterService}.
 * @author cdacr
 *
 */
@RunWith(Parameterized.class)
@PrepareForTest({ RosterDao.class, RosterServiceImpl.class })
public class RosterServiceTest {

	/** Initialize PowerMockito. */
	@Rule
	public PowerMockRule rule = new PowerMockRule();

	/** */
	private RosterService rosterService;

	/**
	 * Create new roster service object for every test case.
	 */
	@Before
	public void setup() {
		rosterService = new RosterServiceImpl();
	}

	/**
	 * Release RosterService object.
	 */
	@After
	public void tear() {
		rosterService = null;
	}

	/**
	 * Test {@link RosterService#getRoster(String)}.
	 */
	@Test
	public void testGetRosterString() {
		final List<Volunteer> volunteers = getVolunteers();
		final RosterDao rosterDao = PowerMockito.mock(RosterDao.class);
		Whitebox.setInternalState(rosterService, RosterDao.class, rosterDao);
		try {
			PowerMockito.when(rosterDao.getVolunteerInfo(Mockito.anyString()))
					.thenReturn(volunteers);
		} catch (final IOException exception) {
			Assert.fail("Must not throw exception");
		}
		final Map<DayAndShift, List<Volunteer>> roster = rosterService
				.getRoster(Constants.ROSTER_FILE_NM);
		assertNotNull("Roster must not null", roster);
	}

	/**
	 * Fetch sample volunteers.
	 * @return {@link List} of {@link Volunteer}
	 */
	private List<Volunteer> getVolunteers() {
		final List<Volunteer> volunteers = new ArrayList<>();
		final Volunteer andrew = new Volunteer();
		andrew.setName("Andrew");
		andrew.addAvailability(DayAndShift.MONDAY_AFTERNOON);
		andrew.addAvailability(DayAndShift.TUESDAY_EITHER);
		andrew.addAvailability(DayAndShift.WEDNESDAY_EITHER);
		andrew.addAvailability(DayAndShift.THURSDAY_EITHER);
		andrew.addAvailability(DayAndShift.FRIDAY_EITHER);

		volunteers.add(andrew);

		final Volunteer raj = new Volunteer();
		raj.setName("Raj");
		raj.addAvailability(DayAndShift.MONDAY_AFTERNOON);
		raj.addAvailability(DayAndShift.TUESDAY_AFTERNOON);
		raj.addAvailability(DayAndShift.WEDNESDAY_AFTERNOON);
		raj.addAvailability(DayAndShift.THURSDAY_AFTERNOON);

		volunteers.add(raj);

		final Volunteer eva = new Volunteer();
		eva.setName("Eva");
		eva.addAvailability(DayAndShift.MONDAY_EITHER);
		eva.addAvailability(DayAndShift.TUESDAY_EITHER);
		eva.addAvailability(DayAndShift.THURSDAY_MORNING);
		eva.addAvailability(DayAndShift.FRIDAY_AFTERNOON);

		volunteers.add(eva);

		final Volunteer mike = new Volunteer();
		mike.setName("Mike");
		mike.addAvailability(DayAndShift.MONDAY_MORNING);
		mike.addAvailability(DayAndShift.TUESDAY_EITHER);
		mike.addAvailability(DayAndShift.WEDNESDAY_EITHER);
		mike.addAvailability(DayAndShift.THURSDAY_EITHER);
		mike.addAvailability(DayAndShift.FRIDAY_EITHER);

		volunteers.add(mike);

		final Volunteer john = new Volunteer();
		john.setName("John");
		john.addAvailability(DayAndShift.WEDNESDAY_MORNING);
		john.addAvailability(DayAndShift.THURSDAY_MORNING);
		john.addAvailability(DayAndShift.FRIDAY_MORNING);

		volunteers.add(john);

		final Volunteer ravi = new Volunteer();
		ravi.setName("Ravi");
		ravi.addAvailability(DayAndShift.WEDNESDAY_MORNING);
		ravi.addAvailability(DayAndShift.THURSDAY_MORNING);
		ravi.addAvailability(DayAndShift.FRIDAY_EITHER);

		volunteers.add(ravi);

		final Volunteer kelly = new Volunteer();
		kelly.setName("Kelly");
		kelly.addAvailability(DayAndShift.THURSDAY_MORNING);
		kelly.addAvailability(DayAndShift.FRIDAY_MORNING);

		volunteers.add(kelly);

		final Volunteer jane = new Volunteer();
		jane.setName("Jane");
		jane.addAvailability(DayAndShift.MONDAY_AFTERNOON);
		jane.addAvailability(DayAndShift.WEDNESDAY_AFTERNOON);

		volunteers.add(jane);

		return volunteers;
	}

	/**
	 * Test {@link RosterService#getSortedShiftCount}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testGetSortedShiftCount() {
		final Map<Volunteer, Integer> shiftCountMap = new HashMap<>();
		final Volunteer v1 = new Volunteer();
		v1.setName("v1");
		shiftCountMap.put(v1, 3);

		final Volunteer v2 = new Volunteer();
		v2.setName("v2");
		shiftCountMap.put(v2, 1);

		final Volunteer v3 = new Volunteer();
		v3.setName("v3");
		shiftCountMap.put(v3, 2);

		Method privateMethod;
		try {
			privateMethod = RosterServiceImpl.class.getDeclaredMethod(
					"getSortedShiftCount", Map.class);
			privateMethod.setAccessible(true);
			final Map<Volunteer, Integer> sortedMap = (Map<Volunteer, Integer>) privateMethod
					.invoke(rosterService, shiftCountMap);
			assertNotNull(sortedMap);
			final Set<Volunteer> keys = sortedMap.keySet();
			int counter = 1;
			for (final Volunteer key : keys) {
				assertThat(sortedMap.get(key), is(counter));
				counter++;
			}
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			fail("Must not throw exception");
		}
	}

	/**
	 * Test {@link RosterServiceImpl#setDeafultShiftCountMapData}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testSetDeafultShiftCountMapData() {
		final Map<Volunteer, Integer> shiftMap = new HashMap<>();
		final List<Volunteer> volunteers = new ArrayList<>();
		final Volunteer v1 = new Volunteer();
		v1.setName("v1");

		final Volunteer v2 = new Volunteer();
		v2.setName("v2");

		final Volunteer v3 = new Volunteer();
		v3.setName("v3");

		volunteers.add(v1);
		volunteers.add(v2);
		volunteers.add(v3);

		Method privateMethod;
		try {
			privateMethod = RosterServiceImpl.class.getDeclaredMethod(
					"setDeafultShiftCountMapData", Map.class, List.class);
			privateMethod.setAccessible(true);
			privateMethod.invoke(rosterService, shiftMap, volunteers);
			assertNotNull(shiftMap);
			assertThat(shiftMap.size(), is(3));
			for (final Volunteer volunteer : volunteers) {
				assertThat(shiftMap.get(volunteer), is(0));
			}
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			fail("Must not throw exception");
		}
	}

	@Parameter(0)
	public DayAndShift shift1;
	@Parameter(1)
	public DayAndShift shift2;
	@Parameter(2)
	public int shiftCount;
	@Parameter(3)
	public int expectedSize;

	/**
	 * Test data.
	 * @return
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays
				.asList(new Object[][] {
						{ DayAndShift.MONDAY_AFTERNOON,
								DayAndShift.MONDAY_AFTERNOON, 1, 1 },
						{ DayAndShift.MONDAY_AFTERNOON,
								DayAndShift.MONDAY_AFTERNOON, 3, 0 },
						{ DayAndShift.MONDAY_EITHER,
								DayAndShift.MONDAY_AFTERNOON, 1, 1 },
						{ DayAndShift.MONDAY_EITHER,
								DayAndShift.MONDAY_AFTERNOON, 3, 0 },
						{ DayAndShift.MONDAY_MORNING,
								DayAndShift.MONDAY_MORNING, 1, 1 },
						{ DayAndShift.MONDAY_MORNING,
								DayAndShift.MONDAY_MORNING, 3, 0 } });
	}

	/**
	 * Test {@link RosterServiceImpl#processUnallocatedVolunteers(Map, Map)}
	 */
	@Test
	public void testProcessUnallocatedVolunteers() {
		final Volunteer v1 = new Volunteer();
		v1.setName("v1");
		v1.addAvailability(shift1);

		final Map<Volunteer, Integer> shiftCountMap = new HashMap<>();
		shiftCountMap.put(v1, shiftCount);
		final Map<DayAndShift, List<Volunteer>> rosterMap = new HashMap<>();
		rosterMap.put(shift2, new ArrayList<Volunteer>());

		Method privateMethod;
		try {
			privateMethod = RosterServiceImpl.class.getDeclaredMethod(
					"processUnallocatedVolunteers", Map.class, Map.class);
			privateMethod.setAccessible(true);
			privateMethod.invoke(rosterService, shiftCountMap, rosterMap);
			assertNotNull(rosterMap);
			assertTrue(rosterMap.containsKey(shift2));
			assertThat(rosterMap.get(shift2).size(), is(expectedSize));
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			fail("Must not throw exception");
		}
	}
}
