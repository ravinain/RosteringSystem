package org.roster.dao.it;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.roster.dao.RosterDao;
import org.roster.dao.impl.RosterDaoImpl;
import org.roster.model.Volunteer;
import org.roster.util.Constants;

/**
 * {@link RosterDao} IT.
 * @author cdacr
 */
public class RosterDaoIT {

	/** */
	private RosterDao rosterDao;

	/** Expect exception rule. */
	@Rule
	public ExpectedException expect = ExpectedException.none();

	/**
	 * Setup for every test case.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		rosterDao = new RosterDaoImpl();
	}

	/**
	 * Release resources after executing test case.
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		rosterDao = null;
	}

	/**
	 * Test {@link RosterDao#getVolunteerInfo(String)}.
	 */
	@Test
	public void testGetVolunteerInfo() {
		try {
			final List<Volunteer> volunteers = rosterDao
					.getVolunteerInfo(Constants.ROSTER_FILE_NM);
			assertNotNull(volunteers);
			assertThat(volunteers.size(), is(8));
		} catch (final IOException e) {
			fail("Must not throw exception!");
		}
	}

	/**
	 * Test {@link RosterDao#getVolunteerInfo(String)} exception scenario.
	 * @throws IOException
	 */
	@Test
	public void testgetVolunteerInfoException() throws IOException {
		expect.expect(IOException.class);
		expect.expectMessage("The system cannot find the file specified");
		rosterDao.getVolunteerInfo(Constants.ROSTER_FILE_NM + "09");
	}
}
