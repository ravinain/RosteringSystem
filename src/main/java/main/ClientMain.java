package main;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.roster.model.DayAndShift;
import org.roster.model.Volunteer;
import org.roster.service.RosterService;
import org.roster.service.impl.RosterServiceImpl;
import org.roster.util.Constants;

/**
 * Client class to call the service method print the data on console.
 * @author cdacr
 */
public final class ClientMain {

	/**
	 * Entry method.
	 * @param args if specified then first argument would be file name.
	 */
	public static void main(final String[] args) {
		String fileName = Constants.ROSTER_FILE_NM;
		if (args != null && args.length > 0) {
			fileName = args[0];
		}
		final RosterService rosterService = new RosterServiceImpl();
		final Map<DayAndShift, List<Volunteer>> roster = new TreeMap<DayAndShift, List<Volunteer>>(
				rosterService.getRoster(fileName));
		final Set<DayAndShift> keys = roster.keySet();
		// TODO: Need to add comparator to display data in sequence.
		for (final DayAndShift key : keys) {
			System.out.format("%s \t\t %s", key, roster.get(key));
			System.out.println();
		}
	}

}
