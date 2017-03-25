package org.roster.service.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.roster.model.DayAndShift;
import org.roster.model.Volunteer;
import org.roster.service.RosterService;
import org.roster.service.impl.RosterServiceImpl;
import org.roster.util.Constants;

/**
 * RosterService IT.
 * @author cdacr
 *
 */
public class RosterServiceIT {

	/** */
	private RosterService rosterService;
	/** Store console output. */
	private final ByteArrayOutputStream content = new ByteArrayOutputStream();
	/** Stream for console output. */
	private PrintStream printStream = null;

	/**
	 * Setup for every test case.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		rosterService = new RosterServiceImpl();
		printStream = new PrintStream(content);
		System.setErr(printStream);
	}

	/**
	 * Reset/Release after execution of test case.
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		rosterService = null;
		printStream = null;
		System.setErr(null);
	}

	/**
	 * Test {@link RosterService#getRoster(String)}.
	 */
	@Test
	public void testGetRoster() {
		final Map<DayAndShift, List<Volunteer>> rosterMap = rosterService
				.getRoster(Constants.ROSTER_FILE_NM);
		assertNotNull(rosterMap);
		assertThat(rosterMap.size(), is(10));
	}

	/**
	 * Test {@link RosterService#getRoster(String)} exception scenario.
	 */
	@Test
	public void testGetRosterException() {
		final Map<DayAndShift, List<Volunteer>> rosterMap = rosterService
				.getRoster(Constants.ROSTER_FILE_NM + "09");
		assertNull(rosterMap);
		assertThat(content.toString(),
				containsString("The system cannot find the file specified"));
	}

}
