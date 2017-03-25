package org.roster.dao;

import java.io.IOException;
import java.util.List;

import org.roster.dao.impl.RosterDaoImpl;
import org.roster.model.Volunteer;

/**
 * RosterDao.java.
 * @see RosterDaoImpl implementation class.
 * @author cdacr
 */
public interface RosterDao {

	/**
	 * Fetch volunteer information from file name specified in input parameter.
	 * @param fileName Spreadsheet file name.
	 * @return {@link List} of {@link Volunteer}.
	 * @throws IOException IOException
	 */
	List<Volunteer> getVolunteerInfo(String fileName) throws IOException;
}
