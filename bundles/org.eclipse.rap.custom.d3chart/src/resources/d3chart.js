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
  var element = document.createElement( "div" );
  element.style.position = "absolute";
  element.style.left = "0px";
  element.style.top = "0px";
  element.style.width = "100%";
  element.style.height = "100%";
// TODO flexible width and height
var width = 250;
var height = 200;
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
    var that = this;
    var selection = this._chart.selectAll( "rect" )
      .data( this._items, function( item ) { return item._rwtId; } );
    selection.enter().append( "rect" )
      .attr( "x", 10 )
      .attr( "y", function( item, index ) { return 10 + index * 20; } )
      .attr( "opacity", 1.0 )
      .attr( "width", 0 )
      .attr( "height", 18 )
      .attr( "fill", function( item ) { return item.getColor(); } );
    selection
      .transition()
      .duration( 1000 )
      .attr( "y", function( item, index ) { return 10 + index * 20; } )
      .attr( "width", function( item ) { return that._xScale( item.getValue() ); } )
      .attr( "fill", function( item ) { return item.getColor(); } );
    selection.exit()
      .transition()
      .duration( 400 )
      .attr( "opacity", 0.0 )
      .remove();
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
  }

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
