This is a simple [Logback](http://logback.qos.ch/) appender which pushes logs to [Slack](https://slack.com/) via an incoming webhook url. 
This project is forked from [github.com/maricn/logback-slack-appender](https://github.com/maricn/logback-slack-appender)
Be aware of the limitations of Slack! Slack is not meant to be handling massive error logs.

# How to setup

Add dependency to com.github.thypho0n:logback-slack-appender:1.0.0 in your pom.xml.

Add SlackAppender configuration to logback.xml file

```
	<?xml version="1.0" encoding="UTF-8" ?>
	<configuration>
		...
		<appender name="SLACK" class="com.github.thypho0n.logback.SlackAppender">
			<!-- Slack webhook API url -->
			<url>https://hooks.slack.com/services/T00XXX/BXX0X0X0X0X/X0X0X0X0X0X0XX</url>
			<!-- Formatting (you can use Slack formatting - URL links, code formatting, etc.) -->
			<layout class="ch.qos.logback.classic.PatternLayout">
				<pattern>%-4relative [%thread] %-5level %class - %msg%n</pattern>
			</layout>
			<!-- Username of the messages sender -->
			<username>${HOSTNAME}</username>
		</appender>

		<!-- Currently recommended way of using Slack appender -->
		<appender name="ASYNC_SLACK" class="ch.qos.logback.classic.AsyncAppender">
			<appender-ref ref="SLACK" />
			<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
				<level>ERROR</level>
			</filter>
		</appender>

		<root>
			<level value="ALL" />
			<appender-ref ref="ASYNC_SLACK" />
		</root>

	</configuration>
```
