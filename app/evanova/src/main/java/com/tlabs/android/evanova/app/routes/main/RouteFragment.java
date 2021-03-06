package com.tlabs.android.evanova.app.routes.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.tlabs.android.evanova.mvp.BaseFragment;

import org.devfleet.dotlan.DotlanOptions;
import org.devfleet.dotlan.DotlanRoute;

import java.util.List;

public class RouteFragment extends BaseFragment {

    private ViewFlipper flipper;
    private RouteInputPager inputPager;
    private RouteDisplayPager displayPager;


    private RoutePresenter displayPresenter;

    public void setPresenter(RoutePresenter displayPresenter) {
        this.displayPresenter = displayPresenter;
    }

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.flipper = new ViewFlipper(getContext());

        this.inputPager = new RouteInputPager(getContext());
        this.inputPager.setListener(new RouteInputPager.Listener() {
            @Override
            public void onRouteSelected(DotlanOptions options) {
                displayPresenter.startView(options);
            }

            @Override
            public void onRouteChanged(List<DotlanOptions> routes) {
                displayPresenter.saveRoutes(routes);
            }
        });

        this.flipper.addView(this.inputPager);

        this.displayPager = new RouteDisplayPager(getContext());
        this.flipper.addView(this.displayPager);
        return flipper;
    }

    public void displayOptions(final List<DotlanOptions> options) {
        this.flipper.setDisplayedChild(0);
        this.inputPager.setRoutes(options);
    }

    public void displayJumps(final DotlanRoute route) {
        this.flipper.setDisplayedChild(1);
        this.displayPager.setRoute(route);
    }

    public void displayRoute(final DotlanRoute route, int type) {
        this.flipper.setDisplayedChild(1);
        this.displayPager.setRoute(route, type);
    }

    boolean onBackPressed() {
        if (this.flipper.getDisplayedChild() == 0) {
            return false;
        }
        this.flipper.setDisplayedChild(0);
        return true;
    }

}
