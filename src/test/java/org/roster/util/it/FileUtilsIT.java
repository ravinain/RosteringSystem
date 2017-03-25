package org.roster.util.it;

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
import org.roster.model.Volunteer;
import org.roster.util.Constants;
import org.roster.util.FileUtils;

public class FileUtilsIT {

	private FileUtils utils;

	@Rule
	public ExpectedException expect = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		utils = new FileUtils();
	}

	@After
	public void tearDown() throws Exception {
		utils = null;
	}

	@Test
	public void testGetVolunteerInfo() {
		try {
			final List<Volunteer> volunteers = utils
					.getVolunteerInfo(Constants.ROSTER_FILE_NM);
			assertNotNull(volunteers);
			assertThat(volunteers.size(), is(8));
		} catch (final IOException e) {
			fail("Must not throw exception!");
		}
	}

	@Test
	public void testgetVolunteerInfoException() throws IOException {
		expect.expect(IOException.class);
		expect.expectMessage("The system cannot find the file specified");
		utils.getVolunteerInfo(Constants.ROSTER_FILE_NM + "09");
	}

}
