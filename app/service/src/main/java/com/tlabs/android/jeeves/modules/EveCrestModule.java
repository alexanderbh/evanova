package com.tlabs.android.jeeves.modules;

import android.content.Context;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;

import org.apache.commons.lang3.StringUtils;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.retrofit.CrestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;

@Module
public class EveCrestModule {
    private static final Logger LOG = LoggerFactory.getLogger(EveCrestModule.class);

    private final Context context;
    private final EveAccount account;

    private CrestService service;

    public EveCrestModule(Context context, final EveAccount account) {
        this.context = context;
        this.account = account;
        this.service = null;
    }

    @Provides
    public CrestService provideCREST() {
        if (null == this.service) {
            this.service = authenticate(this.context, account);
        }
        return this.service;
    }

    private static CrestService authenticate(final Context context, final EveAccount account) {
        if (null == account || StringUtils.isBlank(account.getRefreshToken())) {
            LOG.debug("No account token available; providing public CREST");
            return publicCREST();
        }

        final EveAPIServicePreferences preferences = new EveAPIServicePreferences(context);
        final CrestClient authenticatedCREST =
                CrestClient
                        .TQ(CrestClient.CHARACTER_SCOPES)
                        .id(preferences.getApplicationId())
                        .key(preferences.getApplicationKey())
                        .redirect(preferences.getApplicationRedirect())
                        .build();
        try {
            return authenticatedCREST.fromRefreshToken(account.getRefreshToken());
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            LOG.debug("{}: providing public CREST", e.getLocalizedMessage());
            return publicCREST();
        }
    }

    private static CrestService publicCREST() {
        try {
            return CrestClient.TQ("publicData").build().fromDefault();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            LOG.debug("{}: no public CREST available", e.getLocalizedMessage());
            return null;
        }
    }

}