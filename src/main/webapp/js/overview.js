var data;
var chart = null;
var t = 5, h = 0;
var gridColor = '#444';
var options = {
    curveType: 'function',
    legend: {position: 'top'},
    backgroundColor: 'none',
    //width: 900,
    hAxis: {
        gridlines: {
            color: gridColor, //'none',

            count: -1,

            units: {
                years: {format: ['HH:mm']},
                months: {format: ['HH:mm']},
                days: {format: ['EE HH:mm']},
                hours: {format: ['HH:mm']},
                minutes: {format: ['HH:mm']},
                seconds: {format: ['HH:mm']},
            }

        },
        minorGridlines: {
            color: "#777", //'none',
            units: {
                hours: {format: ['HH:mm']},
                minutes: {format: ['HH:mm']}
            }
        },

        baselineColor: 'none',
        //direction: -1,
        textStyle: {
            color: gridColor
        },
    },
    vAxis: {
        format: '#',
        gridlines: {
            color: gridColor,
        },
        baselineColor: gridColor,
        textStyle: {
            color: gridColor
        }
    },
    series: {
        0: {targetAxisIndex: 0, color: '#CCCC00', visibleInLegend: true},
        1: {targetAxisIndex: 1, color: '#00CCCC', visibleInLegend: true}
    },
    vAxes: {
        0: {title: 'Temps', titleTextStyle: {color: gridColor}},
        1: {title: 'Fuktighet', titleTextStyle: {color: gridColor}},
    },
    interpolateNulls: true,
    animation: {
        duration: 500,
        easing: 'out',
    }
};

google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(function () {
    getTempSensors();
});
setInterval(function () {
    updateTempSensors() // this will run after every 5 seconds
}, 5000);
function updateTempSensors() {
    $("#temprow").children().each(function (index, element) {
        var id = element.id;
        displayTempratureForSensor(id.split("_")[1]);
    });
}
function getTempSensors() {
    $.ajax({
        url: '/data/templog?type=TEMP_TEST',
        dataType: 'json',
    }).done(function (msg) {
        //console.log(msg);
        msg.forEach(function (entry) {
            displayTempratureForSensor(entry.id);
        });
    }).fail(function () {
        console.log("Could't get temp sensors");
    });
};

function displayTempratureForSensor(id) {
    $.ajax({
        url: '/data/templog/' + id,
        dataType: 'json',
    }).done(function (msg) {
        console.log(msg);
        var location = drawTempratureBox(msg);

        if (msg.events && msg.events[0]) {
            plotTempChart(msg.events, location);
        }
    }).fail(function () {
        console.log("Could't get temp sensor data for sensor: " + id);
    });
}

function extractLatestTemprature(msg) {
    var degree = "-";
    if (msg.events && msg.events[0]) {
        $.each(msg.events, function (index, event) {
            if (event && event.values && event.values[0]) {
                $.each(event.values, function (index, value) {
                    if (value.key == "TEMP") {
                        degree = Math.round(value.value * 10) / 10;
                        return false; //break each loop
                    }
                });
            }
            if (degree != "-") { //temp found, break each loop
                return false;
            }
        });
    }
    return degree;
}
function drawTempratureBox(msg) {
    var temprow = document.getElementById("temprow");
    var id = "tempBox_" + msg.id;
    var degree = extractLatestTemprature(msg);

    if (document.getElementById(id)) {
        $("#" + id).find(".degrees").html(degree + "°C")
    } else {
        var col = document.createElement("div");
        col.className = col.className + "col-xs-6 col-md-3";
        col.id = id;

        temprow.appendChild(col);
        var box = document.createElement("div");
        box.className = box.className + "box clearfix";

        col.appendChild(box);
        var name = document.createElement("h2");
        name.appendChild(document.createTextNode(msg.name));

        box.appendChild(name);
        var temp = document.createElement("h2");
        temp.className = temp.className + "degrees " + (degree >= 0 ? "warm" : "cold");
        temp.appendChild(document.createTextNode(degree + "°C"));
        box.appendChild(temp);

        var chart = document.createElement("div");
        chart.className = chart.className + "no_mobile chart";
        box.appendChild(chart);
    }
    ;
    return id;
}
function plotTempChart(events, documentLocation) {
    data = new google.visualization.DataTable();
    data.addColumn('datetime', '');
    data.addColumn('number', 'Tempratur');
    data.addColumn('number', 'Fuktighet');
    $.each(events, function (index, event) {
        if (event && event.regTime && event.values && event.values[0]) {
            var date = new Date(event.regTime);

            var d = new Date();
            d.setDate(d.getDate() - 1);

            if (date > d) { //only display last 24 hours
                var temp = null, humid = null;
                $.each(event.values, function (index, value) {
                    if (value.key == "TEMP") {
                        temp = new Number(value.value).valueOf() + t;
                    } else if (value.key == "HUMIDITY") {
                        humid = new Number(value.value).valueOf() + h;
                    }
                });
                data.addRow([date, temp, humid]);
            }
        }
    });

    if (chart == null) {
        chart = new google.visualization.LineChart($("#" + documentLocation).find(".chart")[0]);
    }
    chart.draw(data, options);
    //chart.draw(data, classicOptions);
//chart.draw(view, {});
}
