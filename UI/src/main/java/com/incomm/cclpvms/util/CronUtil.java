package com.incomm.cclpvms.util;

import static com.cronutils.model.field.expression.FieldExpressionFactory.always;
import static com.cronutils.model.field.expression.FieldExpressionFactory.and;
import static com.cronutils.model.field.expression.FieldExpressionFactory.questionMark;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.CronExpression;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.FieldExpressionFactory;
import com.cronutils.parser.CronParser;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.CronFrequency;


/**
 * @author ramaprabhur
 */
public class CronUtil {

	private CronUtil() {

	}
	private static final Logger logger = LogManager.getLogger(CronUtil.class);
	
	/**
	 * method to build cron expression from user input
	 * @param schedule
	 */
	public static void buildCron(CronSchedule schedule) {
		logger.info(CCLPConstants.ENTER);
		String cronAsString = null;
		Cron cron = null;

		switch(CronFrequency.byFrequencyShortName(schedule.getFrequency())) {
		
		case DAY:
			cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
		    .withYear(always())
		    .withDoW(questionMark())
		    .withMonth(always())
		    .withDoM(FieldExpressionFactory.every(always(), schedule.getInterval()))
		    .withHour(always())
		    .withMinute(always())
		    .withSecond(always())
		    .instance();
			
			// Obtain the string expression
			cronAsString = cron.asString(); // 0 * * L-3 * ? *
			schedule.setCron(cronAsString);
			break;
		case DAY_OF_MONTH:
			List<FieldExpression> daysOfMonth = new ArrayList<>();
			for (Iterator<Integer> iterator = schedule.getDayOfMonth().iterator(); iterator.hasNext();) {
				FieldExpression fieldExpression = FieldExpressionFactory.on(iterator.next());
				daysOfMonth.add(fieldExpression);
				
			}
			cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
		    .withYear(always())
		    .withDoW(questionMark())
		    .withMonth(always())
		    .withDoM(and(daysOfMonth))
		    .withHour(always())
		    .withMinute(always())
		    .withSecond(always())
		    .instance();
			
			// Obtain the string expression
			cronAsString = cron.asString(); // 0 * * L-3 * ? *
			schedule.setCron(cronAsString);		
			break;
		case QUARTER:
			daysOfMonth = new ArrayList<>();
			for (Iterator<Integer> iterator = schedule.getDayOfMonth().iterator(); iterator.hasNext();) {
				FieldExpression fieldExpression = FieldExpressionFactory.on(iterator.next());
				daysOfMonth.add(fieldExpression);
				
			}
			cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
		    .withYear(always())
		    .withDoW(questionMark())
		    .withMonth(FieldExpressionFactory.every(FieldExpressionFactory.on(schedule.getMonth()), 3))
		    .withDoM(and(daysOfMonth))
		    .withHour(always())
		    .withMinute(always())
		    .withSecond(always())
		    .instance();
			
			// Obtain the string expression
			cronAsString = cron.asString(); // 0 * * L-3 * ? *
			schedule.setCron(cronAsString);
			break;
		case YEAR:
			daysOfMonth = new ArrayList<>();
			for (Iterator<Integer> iterator = schedule.getDayOfMonth().iterator(); iterator.hasNext();) {
				FieldExpression fieldExpression = FieldExpressionFactory.on(iterator.next());
				daysOfMonth.add(fieldExpression);
				
			}
			cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
		    .withYear(always())
		    .withDoW(questionMark())
		    .withMonth(FieldExpressionFactory.every(FieldExpressionFactory.on(schedule.getMonth()), 12))
		    .withDoM(and(daysOfMonth))
		    .withHour(always())
		    .withMinute(always())
		    .withSecond(always())
		    .instance();
			
			// Obtain the string expression
			cronAsString = cron.asString(); // 0 * * L-3 * ? *
			schedule.setCron(cronAsString);
			break;
		default:
				
		}
		
		CronExpression exp =null;
		Date nextDate= null;
		try {
			exp = new CronExpression(cronAsString);
			nextDate=exp.getNextValidTimeAfter(new Date());
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String strDate = formatter.format(nextDate);
			schedule.setNextRunDate(strDate);
		} catch (ParseException e) {
			logger.error("Next run Date parsing Exception"+e);
		}	
			
		logger.debug("Frequency: {}, Cron: {}, NextRunDate: {}", schedule.getFrequency(), schedule.getCron(),
				schedule.getNextRunDate());
		
		logger.info(CCLPConstants.EXIT);
	}
	
	/**
	 * method to parse cron expression into user input
	 * @param schedule
	 */
	public static void parseCron(CronSchedule schedule) {		
		logger.info(CCLPConstants.ENTER);
		
		CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
		// Create a parser based on provided definition
		CronParser parser = new CronParser(cronDefinition);
		
		Cron quartzCron = parser.parse(schedule.getCron());
		
		logger.debug("Frequency: {}, Cron: {}", schedule.getFrequency(), schedule.getCron());
		
		switch(CronFrequency.byFrequencyShortName(schedule.getFrequency())) {
		case DAY:			
			String[] dayOfMonth = quartzCron.retrieve(CronFieldName.DAY_OF_MONTH).getExpression().asString().split("/");			
			schedule.setInterval(Integer.parseInt(dayOfMonth[1]));			
			break;
		case DAY_OF_MONTH:
			dayOfMonth = quartzCron.retrieve(CronFieldName.DAY_OF_MONTH).getExpression().asString().split(",");
			List<Integer> days = new ArrayList<>();
			for (int i = 0; i < dayOfMonth.length; i++) {
				days.add(Integer.parseInt(dayOfMonth[i]));
			}
			schedule.setDayOfMonth(days);
			break;
		case QUARTER:
			int dom = Integer.parseInt(quartzCron.retrieve(CronFieldName.DAY_OF_MONTH).getExpression().asString());
			String[] month = quartzCron.retrieve(CronFieldName.MONTH).getExpression().asString().split("/");
			
			days = new ArrayList<>();
			days.add(dom);
			schedule.setDayOfMonth(days);
			schedule.setMonth(Integer.parseInt(month[0]));
			break;
		case YEAR:
			dom = Integer.parseInt(quartzCron.retrieve(CronFieldName.DAY_OF_MONTH).getExpression().asString());
			month = quartzCron.retrieve(CronFieldName.MONTH).getExpression().asString().split("/");
			
			days = new ArrayList<>();
			days.add(dom);
			schedule.setDayOfMonth(days);
			schedule.setMonth(Integer.parseInt(month[0]));
			break;
		default:
		}
		
		logger.info(CCLPConstants.EXIT);
	}
	
	public static Map<String, String> convertFrequencyDtlsFromCron(final String frequency, final String cronExp, final String jobType) {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> frequencyDtls = new HashMap<>();
		
		CronSchedule schedule = new CronSchedule();
		schedule.setFrequency(frequency);
		schedule.setCron(cronExp);
		
		CronUtil.parseCron(schedule);
		if ("day".equals(frequency))
			frequencyDtls.put(jobType+"FreqDay", Integer.toString(schedule.getInterval()));
		else if ("dayOfMonth".equals(frequency)) {
			frequencyDtls.put(jobType+"FreqDayOfMonthMulti", String.valueOf(schedule.getDayOfMonth()).replaceAll("\\[", "").replaceAll("\\]",""));			
		}else {
			frequencyDtls.put(jobType+"FreqDOM", Integer.toString(schedule.getDayOfMonth().get(0)));
			frequencyDtls.put(jobType+"FreqMonth", Integer.toString(schedule.getMonth()));
		}
		logger.info(CCLPConstants.EXIT);
		return frequencyDtls;
	}
	
	public static CronSchedule buildCronSchedule(String frequency, String interval, Object dayOfMonths, String dayOfMonth, String month) {
		logger.info(CCLPConstants.ENTER);
		CronSchedule schedule = new CronSchedule();
		schedule.setFrequency(frequency);
		switch (CronFrequency.byFrequencyShortName(frequency)) {
		case DAY:
			schedule.setInterval(Integer.valueOf(interval));
			break;

		case DAY_OF_MONTH:
			if (dayOfMonths instanceof String[]) {
				schedule.setDayOfMonth(Arrays.asList((String[])dayOfMonths).stream()
				.map(Integer::parseInt).collect(Collectors.toList()));
			} else if (dayOfMonths instanceof String) {
				schedule.setDayOfMonth(Arrays.asList(Integer.valueOf(dayOfMonths.toString())));
			}
			break;
		case QUARTER:
		case YEAR:
			schedule.setDayOfMonth(Arrays.asList(Integer.valueOf(dayOfMonth)));
			schedule.setMonth(Integer.valueOf(month));
			break;

		default:
			break;
		}
		buildCron(schedule);
		logger.info(CCLPConstants.EXIT);
		return schedule;
	}
}
