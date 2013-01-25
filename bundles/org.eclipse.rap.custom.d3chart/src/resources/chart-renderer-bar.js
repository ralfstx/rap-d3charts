/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/

d3chart.Chart.renderers.bar = function( chart ) {
  var barWidth = 25;
  var spacing = 2;
  var xScale = d3.scale.linear().domain( [ 0, 1 ] ).range( [ 0, chart._width - chart._padding * 2 ] );
  var selection = chart._svg.selectAll( "g.barchart_bar" )
    .data( chart._items, function( item ) { return item._rwtId; } );
  var newBars = selection.enter().append( "svg:g" )
    .attr( "class", "barchart_bar" )
    .attr( "opacity", 1.0 );
  newBars.append( "svg:rect" )
    .attr( "x", chart._padding )
    .attr( "y", function( item, index ) { return chart._padding + index * ( barWidth + spacing ); } )
    .attr( "width", 0 )
    .attr( "height", barWidth )
    .attr( "fill", function( item ) { return item.getColor(); } );
  newBars.append( "svg:text" )
    .attr( "text-anchor", "left" )
    .attr( "x", chart._padding + 6 )
    .attr( "y", function( item, index ) { return chart._padding + index * ( barWidth + spacing ) + 18; } )
    .style( "font-family", "sans-serif" )
    .style( "font-size", "12px" );
  selection
    .select( "rect" )
    .transition()
    .duration( 1000 )
    .attr( "y", function( item, index ) { return chart._padding + index * ( barWidth + spacing ); } )
    .attr( "width", function( item ) { return xScale( item.getValue() ); } )
    .attr( "fill", function( item ) { return item.getColor(); } );
  selection
    .select( "text" )
    .transition()
    .duration( 1000 )
    .attr( "x", function( item ) { return chart._padding + 6 + xScale( item.getValue() ); } )
    .attr( "y", function( item, index ) { return chart._padding + index * ( barWidth + spacing ) + 18; } )
    .text( function( item ) { return item.getText(); } );
  selection.exit()
    .transition()
    .duration( 400 )
    .attr( "opacity", 0.0 )
    .remove();
};
