$(document).ready(function(){
  mainchart = $.jqplot('mainchart', [visit_member, visit_non_member, txrebond, txconversion], {
    legend: {
        show: true,
        location: 'ne'
    },
    stackSeries: true,
    title: 'Monthly report',
    series:[{
        label: 'Member visits',
        renderer: $.jqplot.BarRenderer,
        rendererOptions: {
            barWidth: 25
        },
    },{
        label: 'Non member visits',
        renderer: $.jqplot.BarRenderer,
        rendererOptions: {
            barWidth: 25
        },
    },{
        label: 'tx rebond (%)',
        yaxis: 'y2axis',
        lineWidth:2,
        disableStack: true
    },{
        label: 'tx conversion (‰)',
        yaxis: 'y2axis',
        lineWidth:2,
        disableStack: true
    }],
    seriesDefaults: {
        shadowAngle: 135
    },
    axes: {
        xaxis: {
            renderer: $.jqplot.DateAxisRenderer,
            rendererOptions: {
                tickRenderer: $.jqplot.CanvasAxisTickRenderer
            },
            tickInterval: '1 hour',
            tickOptions: {
                formatString: '%a - %Hh',
                fontSize: '10pt',
                angle: -30
            }
        },
        yaxis: {
            min: 0
        },
        y2axis: {
            autoscale:true
        }
    },
    highlighter: {
        show: true,
        sizeAdjust: 7.5
   },
/*
   cursor: {
        show: true,
        zoom:true,
        tooltipLocation: 'sw'
   }*/
});     



  uachart = $.jqplot('uachart', [ua], {
    legend: {
        show: true,
        location: 'ne'
    },
    title: 'Monthly user agent',
    seriesDefaults: {
        renderer: $.jqplot.BarRenderer,
        rendererOptions: {
            //barDirection: 'horizontal',
            barWidth: 25
        },

    },
    axes: {
        xaxis: {
            renderer: $.jqplot.CategoryAxisRenderer,
            rendererOptions: {
                tickRenderer: $.jqplot.CanvasAxisTickRenderer
            },
            tickOptions: {
                fontSize: '10pt',
                angle: -30
            }

        },
        yaxis: {
        },
    },
    highlighter: {
        show: true,
        sizeAdjust: 7.5
   },
/*
   cursor: {
        show: true,
        zoom:true,
        tooltipLocation: 'sw'
   }*/
});     


  refererchart = $.jqplot('refererchart', [netloc_0, netloc_1, netloc_2, netloc_3, netloc_4], {
    legend: {
        show: true,
        location: 'ne'
    },
    title: 'Where do the visitor came from',
    series:[{
        label: netloc_ticks[0],
    }, {
        label: netloc_ticks[1],
    },{
        label: netloc_ticks[2],
    },{
        label: netloc_ticks[3],
    },{
        label: netloc_ticks[4],
    }],
    axes: {
        xaxis: {
            renderer: $.jqplot.DateAxisRenderer,
            rendererOptions: {
                tickRenderer: $.jqplot.CanvasAxisTickRenderer
            },
            tickInterval: '1 hour',
            tickOptions: {
                formatString: '%a - %Hh',
                fontSize: '10pt',
                angle: -30
            }

        },
        yaxis: {
        },
    },
    highlighter: {
        show: true,
        sizeAdjust: 7.5
   },
/*
   cursor: {
        show: true,
        zoom:true,
        tooltipLocation: 'sw'
   }*/
});    

visitsizechart = $.jqplot('visitsizechart', [visit_size], {
      title:'Nb visits by visit size',
        grid:{background:'#fefbf3', borderWidth:2.5},
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            barWidth: 25,
            showMarker: false,
            shadow: false
        },
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer,
                autoscale:false
            }
        },
        series:[{color:'rgba(68, 124, 147, 0.7)'}],
    highlighter: {
        show: true,
        sizeAdjust: 7.5
   },

    });


    
});


