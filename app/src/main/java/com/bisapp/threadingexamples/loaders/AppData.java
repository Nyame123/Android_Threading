package com.bisapp.threadingexamples.loaders;

import java.text.Collator;
import java.util.Comparator;

import android.graphics.drawable.Drawable;

public final class AppData {

  public final String label, path, packageName, data, fileSize;
  public final long size, lastModification;
  public Drawable drawable;

  public AppData(
      String label,
      String path,
      String packageName,
      String data,
      String fileSize,
      long size,
      long lastModification,
      Drawable drawable) {
    this.label = label;
    this.path = path;
    this.packageName = packageName;
    this.data = data;
    this.fileSize = fileSize;
    this.size = size;
    this.lastModification = lastModification;
    this.drawable = drawable;
  }


  public static final class AppDataSorter implements Comparator<AppData> {
    public static final int SORT_NAME = 0, SORT_MODIF = 1, SORT_SIZE = 2;

    private int asc = 1;
    private int sort = 0;

    public AppDataSorter(int sort, int asc) {
      this.asc = asc;
      this.sort = sort;
    }

    /**
     * Compares two elements and return negative, zero and positive integer if first argument is
     * less than, equal to or greater than second
     */
    @Override
    public int compare(AppData file1, AppData file2) {
      if (sort == SORT_NAME) {
        // sort by name
        return asc * file1.label.compareToIgnoreCase(file2.label);
      } else if (sort == SORT_MODIF) {
        // sort by last modified
        return asc * Long.valueOf(file1.lastModification).compareTo(file2.lastModification);
      } else if (sort == SORT_SIZE) {
        // sort by size
        return asc * Long.valueOf(file1.size).compareTo(file2.size);
      }
      return 0;
    }
  }
  /**
   * Perform alphabetical comparison of application entry objects.
   */
  public static final Comparator<AppData> ALPHA_COMPARATOR = new Comparator<AppData>() {
    private final Collator sCollator = Collator.getInstance();
    @Override
    public int compare(AppData object1, AppData object2) {
      return sCollator.compare(object1.label, object2.label);
    }
  };
}
