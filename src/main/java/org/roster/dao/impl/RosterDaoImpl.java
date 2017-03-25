package org.roster.dao.impl;

import java.io.IOException;
import java.util.List;

import org.roster.dao.RosterDao;
import org.roster.model.Volunteer;
import org.roster.util.FileUtils;

/**
 * RosterDaoImpl.java.
 * @see RosterDao
 * @author cdacr
 */
public final class RosterDaoImpl implements RosterDao {

	/** FileUtils. */
	private final FileUtils fileUtils = new FileUtils();

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public List<Volunteer> getVolunteerInfo(final String fileName)
			throws IOException {
		return fileUtils.getVolunteerInfo(fileName);
	}

}
