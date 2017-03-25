package org.roster.model;

/**
 * DayAndShift.java.
 * @author cdacr
 */
public enum DayAndShift {
	/** Monday Shift Enums. */
	MONDAY_MORNING("MONDAY", "MORNING"), MONDAY_AFTERNOON("MONDAY", "AFTERNOON"), MONDAY_EITHER(
			"MONDAY", "EITHER"),
	/** Tuesday Shift Enums. */
	TUESDAY_MORNING("TUESDAY", "MORNING"), TUESDAY_AFTERNOON("TUESDAY",
			"AFTERNOON"), TUESDAY_EITHER("TUESDAY", "EITHER"),
	/** Wednesday Shift Enums. */
	WEDNESDAY_MORNING("WEDNESDAY", "MORNING"), WEDNESDAY_AFTERNOON("WEDNESDAY",
			"AFTERNOON"), WEDNESDAY_EITHER("WEDNESDAY", "EITHER"),
	/** Thursday Shift Enums. */
	THURSDAY_MORNING("THURSDAY", "MORNING"), THURSDAY_AFTERNOON("THURSDAY",
			"AFTERNOON"), THURSDAY_EITHER("THURSDAY", "EITHER"),
	/** Friday Shift Enums. */
	FRIDAY_MORNING("FRIDAY", "MORNING"), FRIDAY_AFTERNOON("FRIDAY", "AFTERNOON"), FRIDAY_EITHER(
			"FRIDAY", "EITHER");

	/** Day. */
	private String day;
	/** Shift. */
	private String shift;

	/**
	 * Set day and shift states.
	 * @param day day e.g. MONDAY
	 * @param shift shift e.g. AFTERNOON
	 */
	DayAndShift(final String day, final String shift) {
		this.day = day;
		this.shift = shift;
	}

	/**
	 * Get day value.
	 * @return day
	 */
	public String getDay() {
		return this.day;
	}

	/**
	 * Get shift value.
	 * @return shift
	 */
	public String getShift() {
		return this.shift;
	}
}
