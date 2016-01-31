var chartList = new Array();
var data;
var chart = null;
var gridColor = '#333';
var options = {
    curveType: 'function',
    legend: {position: 'top'},
    backgroundColor: 'none',
    //width: 300,
    //pointSize: 3,
    hAxis: {
        gridlines: {
            //color: '#000',
            color: gridColor, //'none',
            count: -1,
            units: {
                years: {format: ['YY']},
                months: {format: ['MM']},
                days: {format: ['dd']},
                hours: {format: ['HH']},
                minutes: {format: ['HH:mm', ':mm']},
                seconds: {format: ['ss']},
                milliseconds: {format: ['ss']},
            }
        },
        minorGridlines: {
            count: -1,
            units: {
                years: {format: ['MM']},
                months: {format: ['MM']},
                days: {format: ['dd']},
                hours: {format: ['HH']},
                minutes: {format: [':']},
                seconds: {format: [':']},
                milliseconds: {format: ['ss']},
            },
        },

        baselineColor: 'none',
        //direction: -1,
        textStyle: {
            //color: gridColor,
            color: '#000000',
            fontSize: '17px',
        },
    },
    vAxis: {
        //format: '#.##',
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
        0: {title: 'Temps', format: '#.#', titleTextStyle: {color: gridColor}},
        1: {title: 'Fuktighet', format: '#%', maxValue: 1, minValue: 0, titleTextStyle: {color: gridColor}},
    },
    interpolateNulls: true,
    animation: {
        "startup": true,
        //duration: 1000,
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
        url: '/data/templog?type=TEMP',
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
        //console.log(msg);
        if (msg.events && msg.events[0]) {
            var location = drawTempratureBox(msg);
            plotTempChart(msg.events, location);
        }
    }).fail(function () {
        console.log("Could't get temp sensor data for sensor: " + id);
    });
}

function extractLatestTempratureData(msg) {
    var data = {degree: "-", regtimepretty: ""};

    if (msg.events && msg.events[0]) {
        $.each(msg.events, function (index, event) {
            if (event && event.values && event.values[0]) {
                $.each(event.values, function (index, value) {
                    if (value.key == "TEMP") {
                        data.degree = Math.round(value.value * 10) / 10;
                        data.regtimepretty = event.regTimePretty;
                        //data.regtime = new Date(event.regTimeWithTimeZone);
                        return false; //break each loop
                    }
                });
            }
            if (data.degree != "-") { //temp found, break each loop
                return false;
            }
        });
    }
    return data;
}
function drawTempratureBox(msg) {
    var temprow = document.getElementById("temprow");
    var id = "tempBox_" + msg.id;
    var tempData = extractLatestTempratureData(msg);
    if (document.getElementById(id)) {
        var tempDiv = $("#" + id).find(".degrees");
        tempDiv.html(tempData.degree + "°C");
        tempDiv.removeClass("warm");
        tempDiv.removeClass("cold");
        tempDiv.addClass(tempData.degree >= 0 ? "warm" : "cold");
        $("#" + id).find(".datetime").html(tempData.regtimepretty);
    } else {
        console.log(tempData);
        var col = document.createElement("div");
        col.className = col.className + "col-xs-6 col-md-4";
        col.id = id;

        temprow.appendChild(col);
        var box = document.createElement("div");
        box.className = box.className + "box clearfix";

        col.appendChild(box);
        var name = document.createElement("h2");
        name.appendChild(document.createTextNode(msg.name));
        box.appendChild(name);

        var temp = document.createElement("h2");
        temp.className = temp.className + "degrees " + (tempData.degree >= 0 ? "warm" : "cold");
        temp.appendChild(document.createTextNode(tempData.degree + "°C"));
        box.appendChild(temp);

        var datetime = document.createElement("h6");
        datetime.className = datetime.className + "datetime";
        datetime.appendChild(document.createTextNode(tempData.regtimepretty));
        box.appendChild(datetime);

        var chart = document.createElement("div");
        chart.className = chart.className + "no_mobile chart";
        box.appendChild(chart);
    }
    ;
    return id;
}
function plotTempChart(events, documentLocation) {
    data = new google.visualization.DataTable();
    data.addColumn('datetime', 'Time');
    data.addColumn('number', 'Tempratur');
    data.addColumn('number', 'Fuktighet');
    $.each(events, function (index, event) {
        if (event && event.regTime && event.values && event.values[0]) {
            var date = new Date(event.regTimeWithTimeZone);

            var d = new Date();
            d.setDate(d.getDate() - 1);
            //d.setMinutes(d.getMinutes() - 5);

            if (date > d) { //only display last 24 hours
                var temp = null, humid = null;
                $.each(event.values, function (index, value) {
                    if (value.key == "TEMP" && value.value) {
                        temp = new Number(value.value).valueOf();
                    } else if (value.key == "HUMIDITY" && value.value) {
                        humid = new Number(value.value).valueOf() / 100;
                    }
                });
                data.addRow([date, temp, humid]);
            }
        }
    });
    console.log(chartList);
    if (chartList[documentLocation] == null) {
        chartList[documentLocation] = new google.visualization.LineChart($("#" + documentLocation).find(".chart")[0]);
    }
    //if (chart == null) {
    //    chart = new google.visualization.LineChart($("#" + documentLocation).find(".chart")[0]);
    //}
    chartList[documentLocation].draw(data, options);
    //chart.draw(data, classicOptions);
//chart.draw(view, {});
}
