package com.tlabs.android.evanova.app.routes.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.routes.RouteUseCase;
import com.tlabs.android.jeeves.views.Strings;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.devfleet.dotlan.DotlanJumpOptions;
import org.devfleet.dotlan.DotlanOptions;
import org.devfleet.dotlan.DotlanRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoutePresenter extends EvanovaActivityPresenter<RouteView> {
    private static final Logger LOG = LoggerFactory.getLogger(RoutePresenter.class);
    private final RouteUseCase useCase;

    @Inject
    public RoutePresenter(Context context, RouteUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void setView(RouteView view) {
        super.setView(view);
        setBackgroundDefault();
        subscribe(() -> useCase.loadOptions(), options -> getView().showOptions(options));
    }

    @Override
    public void startView(final Intent startIntent) {
        startView(startOptions(startIntent));
    }

    public void startView(final DotlanOptions options) {
        if (null == options) {
            return;
        }

        if (options instanceof DotlanJumpOptions) {
            setJumpRouteImpl((DotlanJumpOptions)options);
        }
        else {
            setRoutesImpl(options);
        }
    }

    public void saveRoutes(final List<DotlanOptions> routes) {
        subscribe(() -> {this.useCase.saveOptions(routes); return null;});
    }

    private void setJumpRouteImpl(final DotlanJumpOptions options) {
        getView().showLoading(true);
        getView().setTitle(Strings.r(getContext(), R.string.jump_plan_title, options.getFrom(), options.getTo()));
        subscribe(
            () -> {
                final DotlanRoute route = this.useCase.loadJumpRoute(options);
                if (null != route && route.size() > 0) {
                    this.useCase.saveOptions(options);
                }
                return route;
            },
            route -> {
                getView().showLoading(false);
                if (null == route) {
                    getView().setTitleDescription(R.string.jump_plan_unavailable);
                    getView().showRoute(new DotlanRoute());
                }
                else {
                    getView().setTitleDescription(Strings.r(getContext(), R.string.jump_plan_subtitle, route.size()));
                    getView().showRoute(route);
                }
            });
    }

    private void setRoutesImpl(final DotlanOptions options) {
        getView().showLoading(true);

        Observable
            .concat(
                    Observable.defer(() -> Observable.just(this.useCase.loadRoute(options, 0))),
                    Observable.defer(() -> Observable.just(this.useCase.loadRoute(options, 1))),
                    Observable.defer(() -> Observable.just(this.useCase.loadRoute(options, 2))))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<DotlanRoute>() {
                int count = 0;
                boolean save = false;

                @Override
                public void onCompleted() {
                    unsubscribe();
                    if (save) {
                        useCase.saveOptions(options);
                    }
                    getView().showLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    unsubscribe();
                    showError(e.getLocalizedMessage());
                }

                @Override
                public void onNext(DotlanRoute route) {
                    if (null != route && route.size() > 0) {
                        save = true;
                    }
                    getView().showRoute(route, count);
                    count = count + 1;
                }
            });
    }

    private static DotlanOptions startOptions(final Intent intent) {
        if (null == intent) {
            return null;
        }

        final String[] waypoints = intent.getStringArrayExtra(RouteActivity.EXTRA_WAYPOINTS);
        if (ArrayUtils.isEmpty(waypoints)) {
            return null;
        }

        final String jOptions = intent.getStringExtra(RouteActivity.EXTRA_JUMP);
        if (StringUtils.isEmpty(jOptions)) {
            return new DotlanOptions().setWaypoints(Arrays.asList(waypoints));
        }
        final String[] split = StringUtils.split(jOptions, ":");
        if (split.length != 6) {
            return new DotlanOptions().setWaypoints(Arrays.asList(waypoints));
        }
        try {
            return new DotlanJumpOptions()
                    .setJumpShip(split[0])
                    .setAvoidIncursions(Boolean.parseBoolean(split[1]))
                    .setPreferSolarSystems(Boolean.parseBoolean(split[2]))
                    .setJumpDriveCalibrationSkill(Integer.parseInt(split[3]))
                    .setJumpFuelConservationSkill(Integer.parseInt(split[4]))
                    .setJumpFreighterSkill(Integer.parseInt(split[5]))
                    .setWaypoints(Arrays.asList(waypoints));
        }
        catch (NumberFormatException e) {
            LOG.error(e.getLocalizedMessage());
            return new DotlanOptions().setWaypoints(Arrays.asList(waypoints));
        }
    }

}
