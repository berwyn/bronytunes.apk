package com.bronytunes.app.ui.bugreport;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.bronytunes.app.R;

/**
 * With <3 from JakeWharton/u2020
 */
public class BugReportDialog extends AlertDialog implements BugReportview.ReportDetailsListener {
    public interface ReportListener {
        void onBugReportSubmit(BugReportview.Report report);
    }

    private ReportListener listener;

    public BugReportDialog(Context context) {
        super(context);

        final BugReportview view =
                (BugReportview) LayoutInflater.from(context).inflate(R.layout.bugreport_view, null);
        view.setBugReportListener(this);

        setTitle(R.string.title_bug_capture);
        setView(view);
        setButton(Dialog.BUTTON_POSITIVE, context.getString(R.string.btn_submit), (dialog, which) -> {
            if(listener != null) {
                listener.onBugReportSubmit(view.getReport());
            }
        });
        setButton(Dialog.BUTTON_NEGATIVE, context.getString(R.string.btn_cancel), (OnClickListener) null);
    }

    public void setReportListener(ReportListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onStart() {
        getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }

    @Override
    public void onStateChanged(boolean valid) {
        getButton(Dialog.BUTTON_POSITIVE).setEnabled(valid);
    }
}
