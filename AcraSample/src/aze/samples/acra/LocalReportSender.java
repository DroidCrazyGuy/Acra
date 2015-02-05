package aze.samples.acra;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * <p>
 * <b> Name : </b> LocalReportSender
 * </p>
 * <p>
 * <b> Description : </b> This Class Write your Crashed report to specified path
 * </p>
 * 
 * @author SilentKiller
 *
 */
public class LocalReportSender implements ReportSender {

	private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>();
	private FileWriter crashReport = null;
	private File mLogFolder;

	public LocalReportSender(Context ctx, String mDirectoryName, String mFileName) {
		// the destination
		mLogFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "LOG_" + mDirectoryName);
		mLogFolder.mkdirs();
		File logFile = new File(mLogFolder, mFileName);

		try {
			crashReport = new FileWriter(logFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void send(CrashReportData report) throws ReportSenderException {
		final Map<String, String> finalReport = remap(report);

		try {
			BufferedWriter buf = new BufferedWriter(crashReport);

			Set<Entry<String, String>> set = finalReport.entrySet();
			Iterator<Entry<String, String>> i = set.iterator();

			while (i.hasNext()) {
				Map.Entry<String, String> me = (Entry<String, String>) i.next();
				buf.append("[" + me.getKey() + "]=" + me.getValue());
			}

			buf.flush();
			buf.close();
		} catch (IOException e) {
			Log.e("TAG", "IO ERROR", e);
		}
	}

	private Map<String, String> remap(Map<ReportField, String> report) {

		ReportField[] fields = ACRA.getConfig().customReportContent();
		if (fields.length == 0) {
			fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
		}

		final Map<String, String> finalReport = new HashMap<String, String>(report.size());
		for (ReportField field : fields) {
			if (mMapping == null || mMapping.get(field) == null) {
				finalReport.put(field.toString(), report.get(field));
			} else {
				finalReport.put(mMapping.get(field), report.get(field));
			}
		}
		return finalReport;
	}

}