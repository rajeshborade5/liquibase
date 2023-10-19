package in.rajesh.liquibase;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class LiquibaseUtils {

	private LiquibaseUtils() {
	}

	public static <T> T get(ResultSet resultSet, String columnLabel, Class<T> type) throws SQLException {
		var result = resultSet.getObject(columnLabel, type);

		// what a stupid API
		if (resultSet.wasNull()) {
			result = null;
		}
		return result;
	}

	public static Long getLong(ResultSet resultSet, String columnLabel) throws SQLException {
		Long result = resultSet.getLong(columnLabel);

		// what a stupid API
		if (resultSet.wasNull()) {
			result = null;
		}
		return result;
	}

	public static Double getDouble(ResultSet resultSet, String columnLabel) throws SQLException {
		Double result = resultSet.getDouble(columnLabel);

		// what a stupid API
		if (resultSet.wasNull()) {
			result = null;
		}
		return result;
	}

	public static Boolean getBoolean(ResultSet resultSet, String columnLabel) throws SQLException {
		Boolean result = resultSet.getBoolean(columnLabel);

		// what a stupid API
		if (resultSet.wasNull()) {
			result = null;
		}
		return result;
	}

	public static LocalDateTime getLocalDateTime(ResultSet resultSet, String columnLabel) throws SQLException {
		return Optional.ofNullable(get(resultSet, columnLabel, Timestamp.class))
				.map(Timestamp::toLocalDateTime)
				.orElse(null);
	}

	public static LocalTime getLocalTime(ResultSet resultSet, String columnLabel) throws SQLException {
		return Optional.ofNullable(get(resultSet, columnLabel, Time.class))
				.map(Time::toLocalTime)
				.orElse(null);
	}

	public static LocalDate getLocalDate(ResultSet resultSet, String columnLabel) throws SQLException {
		return Optional.ofNullable(get(resultSet, columnLabel, Date.class))
				.map(Date::toLocalDate)
				.orElse(null);
	}

	public static Time toStatement(LocalTime obj) {
		return obj == null ? null : new Time(obj.toSecondOfDay() * 1000L);
	}

	public static java.util.Date toStatement(LocalDateTime obj) {
		return obj == null ? null : java.util.Date.from(obj.toInstant(ZoneOffset.UTC));
	}

}
