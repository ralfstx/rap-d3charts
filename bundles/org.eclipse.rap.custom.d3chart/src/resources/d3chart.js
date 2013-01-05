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

d3chart = {};

// CHART

d3chart.Chart = function() {
  this._type = "bar";
  var element = document.createElement( "div" );
  element.style.position = "absolute";
  element.style.left = "0px";
  element.style.top = "0px";
  element.style.width = "100%";
  element.style.height = "100%";
// TODO flexible width and height
var width = 400;
var height = 300;
  this._element = element;
  this._xScale = d3.scale.linear()
    .domain( [ 0, 1 ] )
    .range( [ 0, width ] );
  this._items = [];
  this._chart = d3.select( this._element ).append( "svg" )
    .attr( "class", "chart" )
    .attr( "width", width )
    .attr( "height", height );
  this._redraw();
  var that = this;
  this._redrawRunner = function() {
    that._redraw();
  };
};

d3chart.Chart.prototype = {

  getElement: function() {
    return this._element;
  },

  setType: function( type ) {
    this._type = type;
    // TODO replace with render callback
    window.setTimeout( this._redrawRunner, 30 );
  },

  getType: function() {
    return this._type;
  },

  _addItem: function( item ) {
    this._items.push( item );
    // TODO replace with render callback
    window.setTimeout( this._redrawRunner, 30 );
  },

  _removeItem: function( item ) {
    this._items = this._items.filter( function( element ) {
      return element !== item;
    } );
    // TODO replace with render callback
    window.setTimeout( this._redrawRunner, 30 );
  },

  _redraw: function() {
    this._renderers[ this._type ]( this );
  },

  _renderers: {

    "bar": function( chart ) {
      var selection = chart._chart.selectAll( "rect" )
        .data( chart._items, function( item ) { return item._rwtId; } );
      selection.enter().append( "rect" )
        .attr( "x", 10 )
        .attr( "y", function( item, index ) { return 10 + index * 30; } )
        .attr( "opacity", 1.0 )
        .attr( "width", 0 )
        .attr( "height", 30 )
        .attr( "fill", function( item ) { return item.getColor(); } );
      selection
        .transition()
        .duration( 1000 )
        .attr( "y", function( item, index ) { return 10 + index * 30; } )
        .attr( "width", function( item ) { return chart._xScale( item.getValue() ); } )
        .attr( "fill", function( item ) { return item.getColor(); } );
      selection.exit()
        .transition()
        .duration( 400 )
        .attr( "opacity", 0.0 )
        .remove();
    },

    "pie": function( chart ) {
      var startAngle = -90;
      var endAngle = 90;
      var layout = d3.layout.pie().sort( null )
        .value( function( item ) { return item.getValue(); } )
        .startAngle( startAngle * Math.PI / 180  )
        .endAngle( endAngle * Math.PI / 180  );
      var arc = d3.svg.arc()
        .outerRadius( 140 )
        .innerRadius( 100 );
      var selection = chart._chart.selectAll( "path.piechart_slice" )
        .data( layout( chart._items ), function( item ) { return item.data._rwtId; } );
      selection
        .attr( "fill", function( item ) { return item.data.getColor(); } )
        .transition()
        .duration( 1000 )
        .attr( "opacity", 1.0 )
        .attrTween( "d", function( datum ) {
          var previous = this._buffer;
          var interpolate = d3.interpolate( previous, datum );
          this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
          return function( t ) {
            return arc( interpolate( t ) );
          };
        })
      ;
      selection.enter().append( "svg:path" )
        .attr( "class", "piechart_slice" )
        .attr( "transform", "translate(" + 200 + "," + 200 + ")" )
        .attr( "opacity", 0.0 )
        .attr( "fill", function( item ) { return item.data.getColor(); } )
        .attr( "d", arc )
        .each( function( datum ) {
          this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
        } )
        .transition()
        .duration( 1500 )
        .attr( "opacity", 1.0 )
      ;
      selection.exit()
        .transition()
        .duration( 500 )
        .attr( "opacity", 0.0 )
        .remove();
    }

  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.Chart", {

  factory : function( properties ) {
    var chart = new d3chart.Chart();
    rap.getObject( properties.parent ).append( chart.getElement() );
    return chart;
  },

  destructor : function( widget ) {
    var el = widget.getElement();
    if( el.parentNode ) {
      el.parentNode.removeChild( el );
    }
  },

  properties: [ "type" ]

} );

// CHART ITEM

d3chart.ChartItem = function( chart ) {
  this._chart = chart;
  this._value = 0;
};

d3chart.ChartItem.prototype = {

  getChart: function() {
    return this._chart;
  },

  getValue: function() {
    return this._value;
  },

  setValue: function( value ) {
    this._value = value;
  },

  getColor: function() {
    return this._color;
  },

  setColor: function( color ) {
    this._color = color;
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.ChartItem", {

  factory : function( properties ) {
    var chart = rap.getObject( properties.parent );
    var item = new d3chart.ChartItem( chart );
    chart._addItem( item );
    return item;
  },

  destructor : function( item ) {
    item.getChart()._removeItem( item );
  },

  properties : [
    "value", "color"
  ],

  propertyHandler: {
    "color": function( chartItem, value ) {
      chartItem.setColor( "#" + rwt.util.Colors.rgbToHexString( value ) );
    }
  }

} );
