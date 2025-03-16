package pl.polsl.screensharing.lib.gui.fragment;

import io.reactivex.rxjava3.core.Observable;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.polsl.screensharing.lib.gui.AbstractTabbedPanel;
import pl.polsl.screensharing.lib.state.AbstractDisposableProvider;

import java.awt.*;

public class JAppTabbedDataChartPanel extends AbstractTabbedPanel {
    private final AbstractDisposableProvider disposableProvider;
    private final Observable<Long> state$;

    private static final int X_TIME_MAX_DATA_SERIES = 100;
    private long time;

    private final XYSeriesCollection dataset;
    private final XYSeries series;
    private final JFreeChart chart;
    private final NumberAxis domainAxis;
    private final XYPlot plot;
    private final ChartPanel chartPanel;

    public JAppTabbedDataChartPanel(AbstractDisposableProvider disposableProvider, Observable<Long> state$) {
        this.disposableProvider = disposableProvider;
        this.state$ = state$;
        time = 0;

        setLayout(new BorderLayout());

        series = new XYSeries(StringUtils.EMPTY);
        dataset = new XYSeriesCollection(series);

        chart = ChartFactory.createXYLineChart(null, "Time (s)", "Data (kb)", dataset,
            PlotOrientation.VERTICAL, false, false, false);

        domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domainAxis.setAutoRange(true);
        domainAxis.setRange(0, X_TIME_MAX_DATA_SERIES);
        domainAxis.setTickLabelsVisible(false);

        plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        chartPanel = new ChartPanel(chart);

        initObservables();

        add(chartPanel, BorderLayout.CENTER);
    }

    public void initObservables() {
        disposableProvider.wrapAsDisposable(state$, bytesPerSecond -> {
            series.add(time, bytesPerSecond.doubleValue() / 1024);
            final NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
            domainAxis.setRange(time - X_TIME_MAX_DATA_SERIES, time);
            time++;
        });
    }
}
