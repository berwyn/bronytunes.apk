package com.bronytunes.app.ui.bugreport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bronytunes.app.BuildConfig;
import com.bronytunes.app.data.LumberYard;
import com.bronytunes.app.util.Intents;
import com.bronytunes.app.util.Strings;
import com.mattprecious.telescope.Lens;

import java.io.File;
import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * With <3 from JakeWharton/u2020
 */
public class BugReportLens implements Lens, BugReportDialog.ReportListener {
    private final Context context;
    private final LumberYard lumberYard;

    private File screenshot;

    public BugReportLens(Context context, LumberYard lumberYard) {
        this.context = context;
        this.lumberYard = lumberYard;
    }

    @Override
    public void onCapture(File file) {
        this.screenshot = file;

        BugReportDialog dialog = new BugReportDialog(context);
        dialog.setReportListener(this);
        dialog.show();
    }

    @Override
    public void onBugReportSubmit(BugReportview.Report report) {
        if(report.includeLogs) {
            lumberYard.save()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(e -> {
                        // TODO: Replace with snackbar?
                        Toast.makeText(context, "Couldn't attach logs.", Toast.LENGTH_SHORT).show();
                        submitReport(report, null);
                    })
                    .subscribe(logs -> {
                        submitReport(report, logs);
                    });
        } else {
            submitReport(report, null);
        }
    }

    private void submitReport(BugReportview.Report report, File logs) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        String densityBucket = getDensityString(dm);

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "berwyn.codeweaver@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, report.title);

        StringBuilder body = new StringBuilder();

        body.append("App:\n")
            .append("Version: ")
            .append(BuildConfig.VERSION_NAME)
            .append(" (")
            .append(BuildConfig.VERSION_CODE)
            .append(")\n\n");

        body.append("Device:\n")
            .append("Make: ").append(Build.MANUFACTURER).append("\n")
            .append("Model: ").append(Build.MODEL).append("\n");

        body.append("Resolution: ")
                .append(dm.widthPixels)
                .append("x")
                .append(dm.heightPixels)
                .append("\n");

        body.append("Density: ")
                .append(dm.densityDpi)
                .append("dpi (")
                .append(densityBucket)
                .append(")\n");

        body.append("Release: ").append(Build.VERSION.RELEASE).append("\n");
        body.append("API: ").append(Build.VERSION.SDK_INT).append("\n");

        if(!Strings.isBlank(report.description)) {
            body.append("\nDescription:\n").append(report.description).append("\n");
        }

        intent.putExtra(Intent.EXTRA_TEXT, body.toString());

        ArrayList<Uri> attachments = new ArrayList<>();
        if(screenshot != null && report.includeScreenshot) {
            attachments.add(Uri.fromFile(screenshot));
        }
        if(logs != null) {
            attachments.add(Uri.fromFile(logs));
        }

        if(!attachments.isEmpty()) {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
        }

        Intents.maybeStartActivity(context, intent);
    }

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return String.valueOf(displayMetrics.densityDpi);
        }
    }
}
