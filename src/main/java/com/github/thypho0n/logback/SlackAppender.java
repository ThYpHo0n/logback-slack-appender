package com.github.thypho0n.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	private static Layout<ILoggingEvent> defaultLayout = new LayoutBase<ILoggingEvent>() {
		public String doLayout(ILoggingEvent event) {
			StringBuffer sbuf = new StringBuffer(128);
			sbuf.append("-- [");
			sbuf.append(event.getLevel());
			sbuf.append("]");
			sbuf.append(event.getLoggerName());
			sbuf.append(" - ");
			sbuf.append(event.getFormattedMessage().replaceAll("\n", "\n\t"));
			return sbuf.toString();
		}
	};

	private String url;

	private Layout<ILoggingEvent> layout = defaultLayout;

	@Override
	protected void append(final ILoggingEvent evt) {
		try {
			final URL url = new URL(this.url);
			final StringWriter w = new StringWriter();

			w.append("{\"text\" : \"").append(JSONObject.quote(layout.doLayout(evt))).append("\"}");

			final byte[] bytes = w.toString().getBytes("UTF-8");

			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestProperty("Content-Type", "application/json");

			final OutputStream os = conn.getOutputStream();
			os.write(bytes);

			os.flush();
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			addError("Error posting log to Slack: " + evt, ex);
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Layout<ILoggingEvent> getLayout() {
		return layout;
	}

	public void setLayout(final Layout<ILoggingEvent> layout) {
		this.layout = layout;
	}
}
