package com.tlabs.android.evanova.app.accounts;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveAccount;

import java.util.List;

public interface AccountView extends ActivityView {

    void setAccount(final EveAccount account);

    void setAccounts(final List<EveAccount> accounts);

    void addAccount(final EveAccount account);

    void removeAccount(final EveAccount account);
}
