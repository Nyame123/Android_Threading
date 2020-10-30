package com.bisapp.threadingexamples.loaders;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.loader.content.AsyncTaskLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppListLoader extends AsyncTaskLoader<AppListLoader.AppsDataPair> {

    private PackageManager packageManager;
    private PackageReceiver packageReceiver;
    private AppsDataPair mApps;

    public AppListLoader(@NonNull Context context) {
        super(context);
        /*
         * Use the getContext() and not the context property since the property context can change
         */
        packageManager = getContext().getPackageManager();
    }

    @Nullable
    @Override
    public AppsDataPair loadInBackground() {
        //retrieve all the installed apps on the device
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(
                PackageManager.MATCH_UNINSTALLED_PACKAGES
                        | PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS);

        //Check if the there isn't any installed apps on the device
        if (apps == null) return new AppsDataPair(Collections.emptyList(), Collections.emptyList());
        mApps = new AppsDataPair(new ArrayList<>(apps.size()), new ArrayList<>(apps.size()));

        //Retrieve the info of individual apps on the device
        for (ApplicationInfo applicationInfo : apps) {
            File sourceDir = new File(applicationInfo.sourceDir);

            //get the name of the application installed
            String label = applicationInfo.loadLabel(packageManager).toString();
            Drawable icon = applicationInfo.loadIcon(packageManager);
            PackageInfo info;

            try {
                info = packageManager.getPackageInfo(applicationInfo.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                info = null;
            }

            AppData elem =
                    new AppData(
                            label == null ? applicationInfo.packageName : label,
                            applicationInfo.sourceDir,
                            applicationInfo.packageName,
                            applicationInfo.flags + "_" + (info != null ? info.versionName : ""),
                            Formatter.formatFileSize(getContext(), sourceDir.length()),
                            sourceDir.length(),
                            sourceDir.lastModified(),
                            icon);

            mApps.first.add(elem);

            Collections.sort(mApps.first, AppData.ALPHA_COMPARATOR);

            for (AppData p : mApps.first) {
                mApps.second.add(p.path);
            }
        }

        return mApps;
    }

    /**
     * We would want to release resources here List is nothing we would want to close
     */
    private void onReleaseResources(AppsDataPair appsDataPair) {
        // like a Cursor, we would close it here.
        appsDataPair = null;
    }

    @Override
    public void deliverResult(@Nullable AppsDataPair data) {
        if (isReset()) {

            if (data != null) onReleaseResources(data);
        }

        // preserving old data for it to be closed
        AppsDataPair oldData = mApps;
        mApps = data;
        if (isStarted()) {
            // loader has been started, if we have data, return immediately
            super.deliverResult(mApps);
        }

        // releasing older resources as we don't need them now
        if (oldData != null) {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mApps != null) {
            // we already have the results, load immediately
            deliverResult(mApps);
        }

        if (packageReceiver == null) {
            packageReceiver = new PackageReceiver(this);
        }

        boolean didConfigChange = InterestingConfigChange.isConfigChanged(getContext().getResources());

        if (takeContentChanged() || mApps == null || didConfigChange) {
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(AppsDataPair data) {
        super.onCancelLoad();
        onReleaseResources(data);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        // we're free to clear resources
        if (mApps != null) {
            onReleaseResources(mApps);
            mApps = null;
        }

        if (packageReceiver != null) {
            getContext().unregisterReceiver(packageReceiver);

            packageReceiver = null;
        }

        InterestingConfigChange.recycle();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    /**
     * typedef Pair<List<AppDataParcelable>, List<String>> AppsDataPair
     */
    public static class AppsDataPair extends Pair<List<AppData>, List<String>> {

        /**
         * Constructor for a Pair.
         *
         * @param first  the first object in the Pair
         * @param second the second object in the pair
         */
        public AppsDataPair(List<AppData> first, List<String> second) {
            super(first, second);
        }
    }

    public static class InterestingConfigChange {

        private static Configuration lastConfiguration = new Configuration();
        private static int lastDensity = -1;

        /**
         * Check for any config change between various callbacks to this method. Make sure to recycle
         * after done
         */
        public static boolean isConfigChanged(Resources resources) {
            int changedFieldsMask = lastConfiguration.updateFrom(resources.getConfiguration());
            boolean densityChanged = lastDensity != resources.getDisplayMetrics().densityDpi;
            int mode =
                    ActivityInfo.CONFIG_SCREEN_LAYOUT
                            | ActivityInfo.CONFIG_UI_MODE
                            | ActivityInfo.CONFIG_LOCALE;
            return densityChanged || (changedFieldsMask & mode) != 0;
        }

        /**
         * Recycle after usage, to avoid getting inconsistent result because of static modifiers
         */
        public static void recycle() {
            lastConfiguration = new Configuration();
            lastDensity = -1;
        }
    }

}
