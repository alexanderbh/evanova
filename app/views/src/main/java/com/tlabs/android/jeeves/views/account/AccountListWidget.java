package com.tlabs.android.jeeves.views.account;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.model.EveAccount;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.Strings;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.R;

import org.apache.commons.lang.StringUtils;


public class AccountListWidget extends AbstractListRecyclerView<EveAccount> {

    public static class Listener implements AbstractListRecyclerView.Listener<EveAccount> {
        @Override
        public void onItemClicked(EveAccount account) {

        }

        @Override
        public void onItemSelected(EveAccount account, boolean selected) {

        }

        @Override
        public void onItemMoved(EveAccount account, int from, int to) {

        }
    }

    private static class AccountHolder extends AbstractListRowHolder<EveAccount> {

        public AccountHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void render(EveAccount account) {
            progress.setVisibility(this.updating ? VISIBLE : GONE);
            if (StringUtils.isBlank(account.getRefreshToken()) && StringUtils.isBlank(account.getKeyID())) {
                renderEmpty(account);
            }
            else {
                renderAuthenticated(account);
            }
        }

        private void renderAuthenticated(EveAccount account) {
            text1.setText(account.getName());

            if (StringUtils.isBlank(account.getStatusMessage())) {
                if (account.getPaidUntil() == 0) {
                    //I18N
                    Strings.r(text5, R.string.jeeves_accounts_noinformation);
                    text5.setTextColor(Color.GRAY);
                }
                else {
                    Strings.r(text5, R.string.jeeves_accounts_paiduntil, EveFormat.DateTime.MEDIUM(account.getPaidUntil()));
                    text5.setTextColor(EveFormat.getDurationColor(account.getPaidUntil() - System.currentTimeMillis()));
                }
            }
            else {
                text5.setText(account.getStatusMessage());
                text5.setTextColor(Color.RED);
            }

            boolean hasSSO = StringUtils.isNotBlank(account.getRefreshToken());
            boolean hasApi = StringUtils.isNotBlank(account.getKeyID());
            if (hasSSO) {
                this.crestImage.setImageResource(R.drawable.ic_crest_enabled);
                this.crestImage.setVisibility(VISIBLE);
                Strings.r(text3, R.string.jeeves_accounts_crest_enabled);
            }
            else if (hasApi) {
                this.crestImage.setImageResource(R.drawable.ic_crest_disabled);
                this.crestImage.setVisibility(VISIBLE);
                Strings.r(text3, R.string.jeeves_accounts_crest_disabled);
            }
            else {
                this.crestImage.setVisibility(INVISIBLE);
            }

            Strings.r(text4, R.string.jeeves_accounts_mask, account.getAccessMask());

            switch (account.getType()) {
                case EveAccount.ACCOUNT: {
                    Strings.r(text2, R.string.jeeves_account_type_account);
                    EveImages.loadCharacterIcon(account.getOwnerId(), this.portraitImage);
                }
                case EveAccount.CHARACTER: {
                    Strings.r(text2, R.string.jeeves_account_type_character);
                    EveImages.loadCharacterIcon(account.getOwnerId(), this.portraitImage);

                }
                break;
                case EveAccount.CORPORATION: {
                    Strings.r(text2, R.string.jeeves_account_type_corporation);
                    EveImages.loadCorporationIcon(account.getOwnerId(), this.portraitImage);
                }
                break;
                default:
                    Strings.r(text2, R.string.jeeves_unknown);
                    this.portraitImage.setImageResource(R.drawable.icon9_64_3_disabled);
                    break;
            }
        }

        //I18N
        private void renderEmpty(EveAccount account) {
            Strings.r(text1, R.string.jeeves_accounts_invalid);
            Strings.r(text2, R.string.jeeves_accounts_reload);
            this.text3.setText("");
            this.text4.setText("");
        }
    }

    public AccountListWidget(Context context) {
        super(context);
    }

    public AccountListWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountListWidget(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected AccountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AccountHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_character, parent, false));
    }

    @Override
    protected long getItemId(final EveAccount item) {
        return item.getId();
    }
}
