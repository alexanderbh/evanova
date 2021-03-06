package com.tlabs.android.evanova.app.mails.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.zkb.ZKillMail;

import java.util.List;

public interface MailView extends ActivityView {

    void setMailBoxes(final List<MailFacade.Mailbox> mailboxes);

    void showMails(final long mailboxID, final List<MailMessage> messages);

    void showMail(final MailMessage message);

    void setKillMails(final List<ZKillMail> killMails, final long ownerID);

    void showKillMail(final ZKillMail killMail, final long ownerID);

    void setNotificationBoxes(final List<MailFacade.Mailbox> mailboxes);

    void showNotifications(final long mailboxID, final List<NotificationMessage> messages);

    void showNotification(final NotificationMessage message);
}
