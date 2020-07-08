package com.incomm.cclp.account.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.isNull;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.exception.ServiceException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;

public class DateTimeUtilTest {

	@Test
	public void testParseZoneDateTime_succeeds() {

		Optional<ZonedDateTime> zoneDateTime;

		zoneDateTime = DateTimeUtil.parse("2016-12-02T11:15:30-05:00[US/Central]");
		assertThat(zoneDateTime.isPresent(), is(true));
		assertThat(zoneDateTime.get()
			.toString(), is("2016-12-02T11:15:30-06:00[US/Central]"));

		zoneDateTime = DateTimeUtil.parse("2016-12-02T11:15:30-05:00");
		assertThat(zoneDateTime.isPresent(), is(true));
		assertThat(zoneDateTime.get()
			.toString(), is("2016-12-02T11:15:30-05:00"));

		zoneDateTime = DateTimeUtil.parse("2016-10-03T15:10:40Z");
		assertThat(zoneDateTime.isPresent(), is(true));
		assertThat(zoneDateTime.get()
			.toString(), is("2016-10-03T15:10:40Z"));

		zoneDateTime = DateTimeUtil.parse("2016-10-02T20:15:30-06:00");
		assertThat(zoneDateTime.isPresent(), is(true));
		assertThat(zoneDateTime.get()
			.toString(), is("2016-10-02T20:15:30-06:00"));

		zoneDateTime = DateTimeUtil.parse("2016-10-02T20:15:30-06:00");
		assertThat(zoneDateTime.isPresent(), is(true));
		assertThat(zoneDateTime.get()
			.toString(), is("2016-10-02T20:15:30-06:00"));

	}

	@Test
	public void testParseZoneDateTime_fails() {

		Optional<ZonedDateTime> zoneDateTime;

		zoneDateTime = DateTimeUtil.parse("2016-12-02T11:15:30");
		assertThat(zoneDateTime.isPresent(), is(false));

		zoneDateTime = DateTimeUtil.parse("2016-12-02");
		assertThat(zoneDateTime.isPresent(), is(false));

		zoneDateTime = DateTimeUtil.parse("Dummy Text");
		assertThat(zoneDateTime.isPresent(), is(false));

		zoneDateTime = DateTimeUtil.parse("");
		assertThat(zoneDateTime.isPresent(), is(false));

		zoneDateTime = DateTimeUtil.parse(null);
		assertThat(zoneDateTime.isPresent(), is(false));

	}

	@Test
	public void testConvert() throws Exception {
		String pacificTime = "2019-08-25T00:00:00-08:00[US/Pacific]";
		ZonedDateTime pacific = ZonedDateTime.parse(pacificTime, DateTimeFormatter.ISO_DATE_TIME);

		String easternTime = pacific.withZoneSameInstant(ZoneId.systemDefault())
			.toLocalDateTime()
			.toString();
		Assert.assertTrue(easternTime.equals("2019-08-25T03:00"));

	}
	
	@Test
	public void compareDatesTest() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = dateFormat.parse("2018-12-06 17:03:00");
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		try {
			endDate = dateFormat.parse("2019-12-06 17:03:00");
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		
		Assert.assertTrue(startDate.before(endDate));
	}
	
	@Test
	public void isFutureDateTest() throws ParseException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date currentDate = new Date(); 
		Date checkDate = dateFormat.parse("2020-12-06 17:03:00");
		Assert.assertTrue(currentDate.before(checkDate));

	}
	
	@Test
	public void isPastDateTest() throws ParseException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date currentDate = new Date(); 
		Date checkDate = dateFormat.parse("2018-12-06 17:03:00");
		Assert.assertTrue(checkDate.before(currentDate));

	}
	
	@Test
	public void convertZdtToLdtTest() {
		ZonedDateTime zonedDatetime = ZonedDateTime.now();
		LocalDateTime localDateTime = null;
		
		LocalDateTime expectedLocalDateTime = zonedDatetime.toLocalDateTime();
		
		localDateTime = zonedDatetime == null ? null
				: zonedDatetime.withZoneSameInstant(ZoneId.systemDefault())
					.toLocalDateTime();
		assertEquals(expectedLocalDateTime,localDateTime);
	}
	
	@Test
	public void convertTestLdtToZdt() {
		LocalDateTime inputLocalDateTime = LocalDateTime.now();
		ZonedDateTime expectedResult = inputLocalDateTime.atZone(ZoneId.of("America/New_York"));
		
		ZonedDateTime actualResult = inputLocalDateTime == null ? null : ZonedDateTime.of(inputLocalDateTime, ZoneId.systemDefault());
		assertEquals(expectedResult,actualResult);
	}
	
	@Test
	public void formatToLocalDateTimeTest() {
		String format = "yyyy-MM-dd"; 
		String inputDate = "2019-11-28T09:30";
		
		LocalDateTime expectedResult = LocalDateTime.of(2019, 11, 28, 0, 0);
		LocalDateTime actualResult = null;
		
		Date date = null;
		try {
			if (inputDate != null && !inputDate.isEmpty()) {
				DateFormat pDfrmt = new SimpleDateFormat(format);
				date = new Date(pDfrmt.parse(inputDate)
					.getTime());
				actualResult =  new Timestamp(date.getTime()).toLocalDateTime();
			}
			else {				
				actualResult = null;
			}
		} catch (ParseException e) {
			throw new ServiceException("Input String date parse error", ResponseCodes.SYSTEM_ERROR);
		}
		
		assertEquals(expectedResult, actualResult);
	}
	
	
	//@Test
	public void parseDateTestStrToDate() {
		String inputDate = "25-Nov-2019"; //current date
		String formatString = "dd-MMM-yyyy";
		DateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		
		Date date = new Date();
		Instant inst = date.toInstant();
	    LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
	    Instant dayInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
	    Date expectedDate = Date.from(dayInst);
		
		Date actualDate = null;
		
		if (isNull(inputDate)) {
			throw new ServiceException("INPUT DATE STRING IS NULL", ResponseCodes.SYSTEM_ERROR);
		}

		try {
			actualDate =  simpleDateFormat.parse(inputDate);
		} catch (ParseException e) {
			System.out.println("tested");
			throw new ServiceException("Unable to parse input date:" + inputDate, ResponseCodes.SYSTEM_ERROR);
		}
		
		assertEquals(expectedDate, actualDate);

	}

	@Test
	public void isEqualZonedTest() {
		ZoneId zoneId = ZoneId.of("America/New_York");
		ZonedDateTime date1 = ZonedDateTime.of(2019, 11, 30, 23, 45, 59, 1234, zoneId);
		ZonedDateTime date2 = ZonedDateTime.of(2019, 11, 30, 23, 45, 59, 1234, zoneId);
//		LocalDateTime date1 = null; 
//		LocalDateTime date2 = null;
		boolean testResult;
		
		if (isNull(date1) && isNull(date2)) {
			testResult = true;
		}
		else if (isNull(date1) && isNotNull(date2) || isNotNull(date1) && isNull(date2)) {
			testResult = false;
		}
		else {
		testResult = date1.toInstant()
				.equals(date2.toInstant());
		}
		
		assertTrue(testResult);

	}
	
	@SuppressWarnings("null")
	@Test
	public void isEqualTest() {
		LocalDateTime date1 = LocalDateTime.of(2019, Month.JULY, 27, 19, 30, 40);
		LocalDateTime date2 = LocalDateTime.of(2019, Month.JULY, 27, 19, 30, 40);
//		LocalDateTime date1 = null; 
//		LocalDateTime date2 = null;
		boolean testResult;
		
		if (isNull(date1) && isNull(date2)) {
			testResult = true;
		}
		else if (isNull(date1) && isNotNull(date2) || isNotNull(date1) && isNull(date2)) {
			testResult = false;
		}
		else {
		testResult = date1.compareTo(date2) == 0;
		}
		
		assertTrue(testResult);
		
	}
	
	@Test
	public void getDefaultZonedIdTest() {
		ZoneId zoneId =  ZoneId.systemDefault();
		String expectedZoneId = "America/New_York";
		assertEquals(expectedZoneId, zoneId.toString());
	}
	
	@Test
	public void toISOStringTest() {
		ZonedDateTime inputDateTime = ZonedDateTime.parse("2020-06-01T04:15:30-04:00[America/New_York]");
		String expectedResult = "2020-06-01T04:15:30-04:00[America/New_York]";
		
		String dateTimeISO = inputDateTime == null ? "" : inputDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		assertEquals(expectedResult, dateTimeISO);
	}

}
