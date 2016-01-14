google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(function () {
    getTempSensors();
});
setInterval(function () {
    updateTempSensors() // this will run after every 5 seconds
}, 2500);
function updateTempSensors() {
    $("#temprow").children().each(function (index, element) {
        var id = element.id;

        displayTempratureForSensor(id.split("_")[1]);
    });
}
function getTempSensors() {
    $.ajax({
        url: '/data/templog',
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
        var location = drawTempratureBox(msg);
        //console.log(location);
        plotTempChart(msg.values, location);
    }).fail(function () {
        console.log("Could't get temp sensor data for sensor: " + id);
    });
}

function drawTempratureBox(msg) {
    var temprow = document.getElementById("temprow");
    var id = "tempBox_" + msg.id;
    var degree = Math.round(msg.values[0][1] * 10) / 10

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
function plotTempChart(msg, documentLocation) {
    var data = new google.visualization.DataTable();
    data.addColumn('number', '');
    data.addColumn('number', '');
    data.addColumn('boolean', 'axis-crossing point');

    msg.forEach(function (entry) {
        data.addRow([entry[0], entry[1], false]);
    });

    var p1, p2, m, b, intersect;
    for (var i = data.getNumberOfRows() - 1; i > 0; i--) {
        p1 = {x: data.getValue(i - 1, 0), y: data.getValue(i - 1, 1)};
        p2 = {x: data.getValue(i, 0), y: data.getValue(i, 1)};

        if ((p1.y >= 0 && p2.y < 0) || (p1.y < 0 && p2.y >= 0)) {
            m = (p2.y - p1.y) / (p2.x - p1.x);
            b = p1.y - m * p1.x;
            intersect = -1 * b / m;
            data.insertRows(i, [
                [intersect, p1.y, true],
                [intersect, p2.y, true]
            ]);
        }
    }

    var view = new google.visualization.DataView(data);
    view.setColumns([0, {
        type: 'number',
        label: 'Positive',
        calc: function (dt, row) {
            var y = dt.getValue(row, 1);
            return data.getValue(row, 2) ? 0 : ((y >= 0) ? y : null);
        }
    }, {
        type: 'number',
        label: 'Negative',
        calc: function (dt, row) {
            var y = dt.getValue(row, 1);
            return data.getValue(row, 2) ? 0 : ((y < 0) ? y : null);
        }
    }]);
    var gridColor = '#444';
    var options = {
        curveType: 'function',
        legend: 'none',
        backgroundColor: 'none',
        hAxis: {
            gridlines: {
                color: 'none',
            },
            baselineColor: 'none',
            direction: -1,
            textStyle: {
                color: gridColor
            },
            //title: 'Timer',
        },
        vAxis: {
            gridlines: {
                color: gridColor
            },
            baselineColor: gridColor,
            textStyle: {
                color: gridColor
            }
        },
        series: {
            0: {color: '#CC0000', visibleInLegend: false},
            1: {color: '#0000CC', visibleInLegend: false}
        },
    };

    var chart = new google.visualization.LineChart($("#" + documentLocation).find(".chart")[0]);
    chart.draw(view, options);
}
