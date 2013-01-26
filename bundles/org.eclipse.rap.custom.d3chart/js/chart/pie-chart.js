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

d3chart.Chart.renderers.pie = function( chart ) {
  var barWidth = 50;
  var startAngle = -90;
  var endAngle = 270;
  var centerX = chart._width / 2;
  var centerY = chart._height / 2;
  var outerRadius = Math.min( chart._width / 2, chart._height - chart._padding ) - chart._padding;
  var innerRadius = Math.max( outerRadius - barWidth, 0 );
  var layout = d3.layout.pie().sort( null )
    .value( function( item ) { return item.getValue(); } )
    .startAngle( startAngle * Math.PI / 180  )
    .endAngle( endAngle * Math.PI / 180  );
  var layer = chart._svg.select( "g.layer" );
  if( layer.empty() ) {
    chart._svg.append( "svg:g" ).attr( "class", "layer" );
    layer = chart._svg.select( "g.layer" );
  }
  layer.attr( "transform", "translate(" + centerX + "," + centerY + ")" );
  var arc = d3.svg.arc()
    .outerRadius( outerRadius )
    .innerRadius( innerRadius );

  var selection = layer.selectAll( "g.piece" )
    .data( layout( chart._items ), function( item ) { return item.data._rwtId; } );

  selection
    .transition()
    .duration( 1000 )
    .attr( "opacity", 1.0 );

  selection
    .select( "path" )
    .transition()
    .duration( 1000 )
    .attr( "fill", function( item ) { return item.data.getColor(); } )
    .attrTween( "d", function( datum ) {
      var previous = this._buffer;
      var interpolate = d3.interpolate( previous, datum );
      this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
      return function( t ) {
        return arc( interpolate( t ) );
      };
    } );

  selection
    .select( "text" )
    .transition()
    .duration( 500 )
    .attr( "opacity", 0.0 )
    .transition()
    .duration( 0 )
    .attr( "transform", function( datum ) { return "translate(" + arc.centroid( datum ) + ")"; } )
    .attr( "dy", ".35em" )
    .attr( "text-anchor", "middle" )
    .text( function( item ) { return item.data.getText(); } )
    .transition()
    .duration( 500 )
    .attr( "opacity", 1.0 )
    ;

  var enter = selection.enter().append( "svg:g" )
    .attr( "class", "piece" )
    .attr( "opacity", 0.0 );

  enter.append( "svg:path" )
    .attr( "fill", function( item ) { return item.data.getColor(); } )
    .attr( "d", arc )
    .each( function( datum ) {
      this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
    } );

  enter.append( "svg:text" )
    .attr( "opacity", 1.0 )
    .attr( "style", "font: 11px sans-serif; fill: white;" )
    .attr( "transform", function( datum ) { return "translate(" + arc.centroid( datum ) + ")"; } )
    .attr( "dy", ".35em" )
    .attr( "text-anchor", "middle" )
    .text( function( item ) { return item.data.getText(); } );

  enter
    .transition()
    .duration( 1500 )
    .attr( "opacity", 1.0 );

  selection.exit()
    .transition()
    .duration( 500 )
    .attr( "opacity", 0.0 )
    .remove();

};
