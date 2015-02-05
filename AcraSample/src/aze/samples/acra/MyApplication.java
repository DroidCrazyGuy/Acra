package aze.samples.acra;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

/**
 * 
 * <p>
 * <b> Name : </b>MyApplication
 * </p>
 * <p>
 * <b> Description : </b>
 * </p>
 * 
 * @author perception
 *
 */
@ReportsCrashes(formKey = "", // will not be used
mailTo = "YOUR_EMAIL_ID_TO_SEND_REPORT", customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION,
		ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT }, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class MyApplication extends Application {

	private String TAG = "SocialKiosk";

	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
		ACRA.getErrorReporter().setReportSender(new LocalReportSender(this, TAG, System.currentTimeMillis() + ".txt"));
	}

}