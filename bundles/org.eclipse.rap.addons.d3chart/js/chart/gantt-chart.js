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

d3chart.GanttChart = function( parent ) {
  // add calendar css
  var head = document.getElementsByTagName('head')[0];
  var ref = document.createElement('link');
  ref.setAttribute('rel', 'stylesheet');
  ref.setAttribute('type', 'text/css');
  ref.setAttribute('href', 'rwt-resources/d3chart/d3chart.css');
  head.appendChild(ref);

  var _FIT_TIME_DOMAIN_MODE = "fit";
  var _FIXED_TIME_DOMAIN_MODE = "fixed";
    
  this._margin = {
	top : 20,
	right : 40,
	bottom : 40,
	left : 50
  };
  this._barHeight = -1;
  this._timeDomainStart = d3.time.day.offset(new Date(),-3);
  this._timeDomainEnd = d3.time.hour.offset(new Date(),+3);
  this._timeDomainMode = _FIXED_TIME_DOMAIN_MODE;// fixed or fit
  this._taskTypes = ["Chrome", "Foxie"];
  //this._taskStatus = {"1":"status1", "2":"status2"};
  this._height = 200 - this._margin.top - this._margin.bottom-5;
  this._width = 200 - this._margin.right - this._margin.left-5;
  this._tickFormat = "%H:%M";
  this._x = d3.time.scale().domain([ this._timeDomainStart, this._timeDomainEnd ]).range([ 0, this._width ]).clamp(true);
  this._y = d3.scale.ordinal().domain(this._taskTypes).rangeRoundBands([ 0, this._height - this._margin.top - this._margin.bottom ], .1);
  this._xAxis = d3.svg.axis().scale(this._x).orient("bottom").tickFormat(d3.time.format(this._tickFormat)).tickSubdivide(true)
	    .tickSize(8).tickPadding(8);
  this._yAxis = d3.svg.axis().scale(this._y).orient("left").tickSize(0);
  this._items = new d3chart.ItemList();
  this._chart = new d3chart.Chart( parent, this );
};

d3chart.GanttChart.prototype = {

  addItem: function( item ) {
	  this._items.add(item);
      this._chart._scheduleUpdate();
  },

  removeItem: function( item ) {
	  this._items.remove( item );
      this._chart._scheduleUpdate();
  },

  destroy: function() {
    this._chart.destroy();
  },

  initialize: function( chart ) {
    this._chart = chart;
    this._layer = chart.getLayer("gantt-chart");
	this._width = chart._width - this._margin.left - this._margin.right;
	this._height = chart._height;
    this._gantt();
  },

  setBarHeight: function(value) {
	  this._barHeight = value;
	  this._redraw();
  },
  getBarHeight: function() {
	  return this._barHeight;
  },
 
  setMargin: function(json){
	  this._margin.top = json.top;
	  this._margin.right = json.right;
	  this._margin.bottom = json.bottom;
	  this._margin.left = json.left;
  },

  setTaskTypes: function(json) {
	  var that = this;
	  this._taskTypes = json;
	  json.forEach(function(v){
		  console.log(v + " " + v.length);
		  var width = v.length + " em";
		  var left = Number(that._margin.left)
		  if (left < Number(v.length)) {
			  that._margin.left = width;
			  console.log(v + " " + width);
		  }
	  });
  },

  setTimeRange: function(json) {
	  this._timeDomainStart = json.from;
	  this._timeDomainEnd = json.to;
	  this._redraw();
  },
  getTimeRange: function() {
	  return [{"from":this._timeDomainStart, "to":this._timeDomainEnd}];
  },

  render: function( chart ) {
	  this._width = chart._width - this._margin.left - this._margin.right;
	  this._height = chart._height;
	  this._redraw();
  },

  _getTasks: function() {
	  var tasks = [];
	  for (var i=0; i < this._items.length; i++) {
		  var values = this._items[i].getValues();
		  tasks.push({
			  "startDate": new Date(values[0]),
			  "endDate": new Date(values[1]),
			  "taskText": this._items[i].getText(),
			  "taskType": this._taskTypes[values[2]],
			  "color": this._items[i].getColor()
		  });
	  }
	  return tasks;
  },
  
  //************************
  _keyFunction: function(d) {
	  return d.startDate + d.taskType + d.endDate;
  },

  _rectTransform: function(d, that) {
	  return "translate(" + that._x(d.startDate) + "," + that._y(d.taskType) + ")";
  },

  _initTimeDomain: function() {
	  if (this._timeDomainMode === this._FIT_TIME_DOMAIN_MODE) {
		  var tasks = this._getTasks();
		  if (tasks === undefined || tasks.length < 1) {
			  this._timeDomainStart = d3.time.day.offset(new Date(), -3);
			  this._timeDomainEnd = d3.time.hour.offset(new Date(), +3);
			  return;
		  }
		  tasks.sort(function(a, b) {
			  return a.endDate - b.endDate;
		  });
		  this._timeDomainEnd = tasks[tasks.length - 1].endDate;
		  tasks.sort(function(a, b) {
			  return a.startDate - b.startDate;
		  });
		  this._timeDomainStart = tasks[0].startDate;
	  }
  },

  _initAxis: function() {
	  this._x = d3.time.scale().domain([ this._timeDomainStart, this._timeDomainEnd ]).range([ 0, this._width ]).clamp(true);
	  this._y = d3.scale.ordinal().domain(this._taskTypes).rangeRoundBands([ 0, this._height - this._margin.top - this._margin.bottom ], .1);
	  this._xAxis = d3.svg.axis().scale(this._x).orient("bottom").tickFormat(d3.time.format(this._tickFormat)).tickSubdivide(true)
	  .tickSize(-this._height).tickPadding(4);

	  this._yAxis = d3.svg.axis().scale(this._y).orient("left").tickSize(0);
	  d3.select(".yAxis").selectAll("text").attr("class", "gantt-chart-label");
  },

  _gantt: function () {
	  this._initTimeDomain();
	  this._initAxis();
	  var tasks = this._getTasks();
	  var that = this;

	  var svg = d3.select("svg")
	  .attr("class", "chart")
	  .attr("width", this._width + this._margin.left + this._margin.right)
	  .attr("height", this._height + this._margin.top + this._margin.bottom);
	  //.append("g")
	  	  
	  //create tooltip if necessary
	  if (d3.select(".tooltip").empty){
		  var div = d3.select(this._chart._element).append("div")   
		    		.attr("class", "tooltip")               
		    		.style("opacity", 0);
	  }
	  //remove all childnodes
	  d3.selectAll("rect").remove();
	  d3.selectAll("g.xAxis").remove();
	  d3.selectAll("g.yAxis").remove();
	  
	  var svg = d3.select("g")
	  .attr("class", "gantt-chart")
	  .attr("width", this._width + this._margin.left + this._margin.right)
	  .attr("height", this._height + this._margin.top + this._margin.bottom)
	  .attr("transform", "translate(" + this._margin.left + ", " + this._margin.top + ")");

	  svg.selectAll(".chart")
	  .data(tasks, this._keyFunction).enter()
	  .append("rect")
	  .attr("rx", 2)
	  .attr("ry", 2)
	  .attr("class", "bar")
	  .attr("y", 0)
	  .attr("transform", function(d){
		  return that._rectTransform(d, that);
	  })
	  .attr("height", function(d) {
		  if (that._barHeight == -1) {
			  return that._y.rangeBand();
		  } else {
			  return that._y.rangeBand();
		  }
	  })
	  .attr("width", function(d) { 
		  return (that._x(d.endDate) - that._x(d.startDate)); 
	  })
	  .attr("fill", function(d){
		  return d.color;
	  })
 	  .on("mouseover", function(d) {
            div.transition()        
                .duration(200)      
                .style("opacity", .9);      
            div.html(d.taskText + "asgasfgadf")  
                .style("left", (that._x(d.startDate) + (that._x(d.endDate)-that._x(d.startDate))/2) + "px")     
                .style("top", (that._y(d.taskType) + 10) + "px");    
      })                  
      .on("mouseout", function(d) {       
            div.transition()        
                .duration(500)      
                .style("opacity", 0);   
      });
	  
	  svg.append("g")
	  .attr("class", "xAxis")
	  .attr("transform", "translate(0, " + (this._height - this._margin.top - this._margin.bottom) + ")")
	  .transition();
	  //.call(this._xAxis);

	  svg.append("g").attr("class", "yAxis").transition().call(this._yAxis);
	  this._xAxis(d3.select(".xAxis"));
	  this._yAxis(d3.select(".yAxis"));
  },
  
  _redraw: function() {
	  this._initTimeDomain();
	  this._initAxis();
	  var tasks = this._getTasks();
	  var that = this;

	  var svg = d3.select("svg");

	  var ganttChartGroup = svg.select(".gantt-chart");
	  var rect = ganttChartGroup.selectAll("rect").data(tasks, that._keyFunction)

	  rect.enter()
	  .insert("rect",":first-child")
	  .attr("rx", 2)
	  .attr("ry", 2)
	  .attr("class", "bar")
	  .transition()
	  .attr("y", 0)
	  .attr("transform", function(d){
		  that._rectTransform(d, that);
	  })
	  .attr("height", function(d) {
		  if (that._barHeight == -1) {
			  return that._y.rangeBand();
		  } else {
			  return that._y.rangeBand();
		  }
	  })
	  .attr("width", function(d) { 
		  return (that._x(d.endDate) - that._x(d.startDate)); 
	  });


	  rect.transition()
	  .attr("transform", function(d){
		  return that._rectTransform(d, that);
	  })
	  .attr("height", function(d) { return that._y.rangeBand(); })
	  .attr("width", function(d) { 
		  return (that._x(d.endDate) - that._x(d.startDate)); 
	  })
	  .attr("fill", function(d){
		  return d.color;
	  });

	  rect.exit().remove();

	  //svg.select(".xAxis").transition().call(this._xAxis);
	  //svg.select(".yAxis").transition().call(this._yAxis);
	  this._xAxis(svg.select(".xAxis"));
	  this._yAxis(svg.select(".yAxis"));
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.GanttChart", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    return new d3chart.GanttChart( parent );
  },
  destructor: "destroy",
  properties: ["timeRange", "barHeight", "margin", "taskTypes"], // [ "barWidth", "spacing" ],
  events: ["Selection"]

} );
