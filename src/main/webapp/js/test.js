var speedometer;
// Explicit onready listener for modern browsers. If you use a JS framework,
// you should replace this code with your toolkit's "onReady" helpers (e.g.
// $(document).ready() on jQuery, Event.observe('dom:loaded') on prototype,
// and so on.
document.addEventListener ("DOMContentLoaded", function() {
    document.removeEventListener ("DOMContentLoaded", arguments.callee, false);

        // Check out configuration and API on GitHub Wiki:
        // http://wiki.github.com/vjt/canvas-speedometer
        var config = {
                         theme: 'temp',
                         meterTicksCount: 8,
                         meterMarksCount: 4,
                         threshold: 0.0,
                         thresholdPivot: -20.0,
                         thresholdWidth: 40,
                         min: -40.0,
                         max: 40.0,
                         value: -40.0};
        var temp_ute = -10;
        var temp_soverom = 13;
        var temp_stue = 23;

        speedometer_ute = new Speedometer ('speedometer_ute', config);
        speedometer_ute.draw ();
        speedometer_ute.animatedUpdate (temp_ute, Math.abs(temp_ute-speedometer_ute.value())*50);

        speedometer_stue = new Speedometer ('speedometer_stue', config);
        speedometer_stue.draw ();
        speedometer_stue.animatedUpdate (temp_soverom, Math.abs(temp_soverom-speedometer_stue.value())*50);

        speedometer_soverom = new Speedometer ('speedometer_soverom', config);
        speedometer_soverom.draw ();
        speedometer_soverom.animatedUpdate (temp_stue, Math.abs(temp_stue-speedometer_soverom.value())*50);
}, false);