D3 Chart widgets for RAP
========================

Experimental chart widgets for [RAP](http://eclipse.org/rap) using the [d3](http://d3js.org) library.

You can see the demo [online](http://rap.eclipsesource.com/rapdemo/examples#chart).

Synopsis
--------

There's an abstract base class `Chart` with subtypes for different chart types like `PieChart`, `BarChart` etc.
Data is added as separate `ChartItem`s, each with its value, color, text, etc.

Example:

```java
PieChart pieChart = new PieChart( composite, SWT.NONE );
pieChart.setInnerRadius( 0.6f );

ChartItem item1 = new ChartItem( pieChart );
item1.setText( "Firefox" );
item1.setColor( colors.get( 0 ) );
item1.setValue( 23 );
```
