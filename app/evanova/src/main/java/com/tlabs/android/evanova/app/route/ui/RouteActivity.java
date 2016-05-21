package com.tlabs.android.evanova.app.route.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.route.DaggerRouteComponent;
import com.tlabs.android.evanova.app.route.RouteModule;
import com.tlabs.android.evanova.app.route.RouteView;
import com.tlabs.android.evanova.app.route.presenter.RoutePresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;

import org.devfleet.dotlan.DotlanOptions;
import org.devfleet.dotlan.DotlanRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

public class RouteActivity extends BaseActivity implements RouteView {
    private static final Logger LOG = LoggerFactory.getLogger(RouteActivity.class);

    public static final String EXTRA_WAYPOINTS = RouteActivity.class.getName() + ".waypoints";//array
    public static final String EXTRA_JUMP = RouteActivity.class.getName() + ".options";//custom format

    @Inject
    RoutePresenter presenter;

    private RouteFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerRouteComponent
            .builder()
            .evanovaComponent(Application.getEveComponent())
            .routeModule(new RouteModule())
            .build()
            .inject(this);

        this.fragment = new RouteFragment();
        this.fragment.setPresenter(this.presenter);

        setFragment(this.fragment);

        this.presenter.setView(this);
        this.presenter.setRoute(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void onBackPressed() {
        if (!this.fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void showRoute(DotlanRoute route) {
        this.fragment.displayJumps(route);
    }

    @Override
    public void showRoute(DotlanRoute route, int type) {
        this.fragment.displayRoute(route, type);
    }


    @Override
    public void showOptions(List<DotlanOptions> options) {
        this.fragment.displayOptions(options);
    }
}
