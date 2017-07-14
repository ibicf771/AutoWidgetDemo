package com.autosimilarwidget.view;


interface IAutoSimilarWidgetViewService {

    void addSurface(in Surface aSurface, int type);

    void removedSurface(in Surface aSurface, int type);

    boolean isMapRunning();
}
